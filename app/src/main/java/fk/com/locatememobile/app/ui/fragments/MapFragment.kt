package fk.com.locatememobile.app.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.transitionseverywhere.Fade
import com.transitionseverywhere.Slide
import com.transitionseverywhere.TransitionManager
import fk.com.locatememobile.app.App
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.rest.dtos.UserFriendDTO
import fk.com.locatememobile.app.ui.contracts.MapFragmentContract
import fk.com.locatememobile.app.ui.MarkerColors
import fk.com.locatememobile.app.ui.adapters.UserAdapter
import fk.com.locatememobile.app.ui.UserSelectedListener
import fk.locateme.app.R
import kotlinx.android.synthetic.main.fragment_map.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class MapFragment : Fragment(), MapFragmentContract.View, UserSelectedListener {
    private val TAG = this::class.java.simpleName
    @Inject
    lateinit var presenter: MapFragmentContract.Presenter
    private lateinit var userMarkerMap: MutableMap<UserFriendDTO, Marker>
    private lateinit var userAdapter: UserAdapter
    private var googleMap: GoogleMap? = null
    private var isDrawerOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userMarkerMap = mutableMapOf()
        (activity.application as App).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        return view
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val m = childFragmentManager.findFragmentById(R.id.map_fragment_map) as SupportMapFragment
        getGoogleMap(m)
        setup()
        presenter.register(this)
    }

    private fun getGoogleMap(m: SupportMapFragment) {
        m.getMapAsync({ map ->
            map.clear()
            googleMap = map
            setMapProperties(googleMap)
        })
    }

    override fun onResume() {
        super.onResume()
        presenter.viewResumed()
    }

    private fun setup() {
        setClickListeners()
        map_fragment_friend_list_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        userAdapter = UserAdapter(this)
        map_fragment_friend_list_recycler_view.adapter = userAdapter
        setSeekBarChangeListener()
        setUpTextSwitcher()
    }

    private fun setUpTextSwitcher() {
        map_fragment_current_location_time_text_switcher.setFactory({
            TextView(ContextThemeWrapper(context, R.style.textSwitcherTextStyle), null, 0)
        })
        val inAnim = AnimationUtils.loadAnimation(activity,
                android.R.anim.fade_in)
        val outAnim = AnimationUtils.loadAnimation(activity,
                android.R.anim.fade_out)

        inAnim.duration = 200
        outAnim.duration = 200
        map_fragment_current_location_time_text_switcher.inAnimation = inAnim
        map_fragment_current_location_time_text_switcher.outAnimation = outAnim
    }

    private fun setClickListeners() {
        map_fragment_bottom_drawer.setOnClickListener {}
        map_fragment_expand_button.setOnClickListener { openCloseBottomDrawer() }
        map_fragment_add_friend_button.setOnClickListener { openAddFriendFragment() }
        map_fragment_seek_time_view_close.setOnClickListener { userSelectionCancelled() }
        map_fragment_refresh_button.setOnClickListener { presenter.onRefresh() }
        map_fragment_settings_button.setOnClickListener { presenter.onSettingsClicked()}
    }

    private fun setSeekBarChangeListener() {
        map_fragment_seek_time_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, p2: Boolean) {
                presenter.onSeekBarValueChanged(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar) {
            }

            override fun onStopTrackingTouch(p0: SeekBar) {
            }

        })
    }

    private fun userSelectionCancelled() {
        presenter.userSelectionCancelled()
        hideSeekBarView()
        showFriendsView()
    }

    override fun showUserFriendsLocations(userFriendsLocationsInBuckets: Array<List<Location?>>, usersMarkerPairs: List<Pair<UserFriendDTO, MarkerColors>>) {
        userFriendsLocationsInBuckets.forEach { createMarkerForLastUserLocation(it, usersMarkerPairs) }
    }

    private fun createMarkerForLastUserLocation(it: List<Location?>, userMarkerPairs: List<Pair<UserFriendDTO, MarkerColors>>) {
        val lastLocation = it.findLast { it != null }
        lastLocation?.let {
            val userMarkerPair = userMarkerPairs.find { it.first.userFriendId == lastLocation.userId }
            if (userMarkerPair != null) {
                val marker = googleMap?.addMarker(getMarkerOptions(lastLocation, userMarkerPair.first, userMarkerPair.second))
                addNewMarkerToMapAndRemoveOld(userMarkerPair.first, marker)
            }
        }
    }

    override fun showUserFriends(userFriendMarkerColorPairs: List<Pair<UserFriendDTO, MarkerColors>>) {
        if (!userFriendMarkerColorPairs.isEmpty()) {
            userAdapter.setUsersWithColors(userFriendMarkerColorPairs)
            hideNoFriendsPlaceholder()
            showFriendsRecyclerView()
        } else {
            hideFriendsRecyclerView()
            showNoFriendsPlaceholder()
        }
    }

    private fun hideNoFriendsPlaceholder() {
        map_fragment_no_friends_yet_text.visibility = View.GONE
    }

    private fun showNoFriendsPlaceholder() {
        map_fragment_no_friends_yet_text.visibility = View.VISIBLE
    }

    private fun showFriendsRecyclerView() {
        map_fragment_friend_list_recycler_view.visibility = View.VISIBLE
    }

    private fun hideFriendsRecyclerView() {
        map_fragment_friend_list_recycler_view.visibility = View.GONE
    }

    private fun openAddFriendFragment() {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrame, AddFriendFragment())
        fragmentTransaction.addToBackStack("MapFragment")
        fragmentTransaction.commit()
    }

    override fun zoomToUserLocation(location: Location) {
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(LatLng(location.latitude, location.longitude), 13f)))
    }

    @SuppressLint("MissingPermission")
    private fun setMapProperties(googleMap: GoogleMap?) {
        googleMap?.isMyLocationEnabled = true
        googleMap?.uiSettings?.isMapToolbarEnabled = false
        googleMap?.setMaxZoomPreference(14f)
        googleMap?.setMinZoomPreference(1f)
    }

    private fun openCloseBottomDrawer() {
        if (isDrawerOpen) {
            hideBottomDrawer()
            isDrawerOpen = false
        } else {
            showBottomDrawer()
            isDrawerOpen = true
        }
    }

    private fun showBottomDrawer() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(main_map_fragment)
        constraintSet.clear(R.id.map_fragment_expand_button, ConstraintSet.BOTTOM)
        constraintSet.clear(R.id.map_fragment_bottom_drawer, ConstraintSet.TOP)
        constraintSet.connect(R.id.map_fragment_bottom_drawer, ConstraintSet.BOTTOM, R.id.main_map_fragment, ConstraintSet.BOTTOM)
        constraintSet.connect(R.id.map_fragment_expand_button, ConstraintSet.BOTTOM, R.id.map_fragment_bottom_drawer, ConstraintSet.TOP)
        TransitionManager.beginDelayedTransition(main_map_fragment, Fade(Fade.MODE_OUT))
        constraintSet.applyTo(main_map_fragment)
    }

    private fun hideBottomDrawer() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(main_map_fragment)
        constraintSet.clear(R.id.map_fragment_expand_button, ConstraintSet.BOTTOM)
        constraintSet.clear(R.id.map_fragment_bottom_drawer, ConstraintSet.BOTTOM)
        constraintSet.connect(R.id.map_fragment_expand_button, ConstraintSet.BOTTOM, R.id.main_map_fragment, ConstraintSet.BOTTOM)
        constraintSet.connect(R.id.map_fragment_bottom_drawer, ConstraintSet.TOP, R.id.map_fragment_expand_button, ConstraintSet.BOTTOM)
        TransitionManager.beginDelayedTransition(main_map_fragment, Slide(Gravity.BOTTOM))
        constraintSet.applyTo(main_map_fragment)
    }

    override fun showTimeSeekBarView() {
        hideFriendsView()
        showSeekBarView()
    }

    private fun showSeekBarView() {
        map_fragment_seek_time_view.visibility = View.VISIBLE
        map_fragment_seek_time_bar.progress = 100
    }


    private fun hideSeekBarView() {
        map_fragment_seek_time_view.visibility = View.GONE
    }

    private fun hideFriendsView() {
        map_fragment_friends_view.visibility = View.GONE
    }


    private fun showFriendsView() {
        map_fragment_friends_view.visibility = View.VISIBLE
    }

    private fun getMarkerOptions(location: Location, user: UserFriendDTO, markerColors: MarkerColors): MarkerOptions {
        return MarkerOptions()
                .position(LatLng(location.latitude, location.longitude))
                .title(user.manufacturer + " " + user.device)
                .icon(BitmapDescriptorFactory.defaultMarker(markerColors.markerHue))
    }


    override fun onUserSelected(user: UserFriendDTO) {
        presenter.userSelected(user)
    }

    override fun onStop() {
        super.onStop()
        googleMap?.clear()
    }

    override fun setToken(token: String) {
        map_fragment_token_text.text = token
    }

    override fun displaySelectedUserFriendLocation(selectedUserFriend: UserFriendDTO, location: Location?, markerColors: MarkerColors) {
        if (location != null) {
            addMarkerAndZoomToUserLocation(selectedUserFriend, location, markerColors)
            map_fragment_current_location_time_text_switcher.setText(formatDate(location.time))
        }
    }

    private fun formatDate(time: Long): String {
        val dateFormatter = SimpleDateFormat("HH:mm")
        return dateFormatter.format(Date(time))
    }

    private fun addMarkerAndZoomToUserLocation(userFriend: UserFriendDTO, location: Location, markerColors: MarkerColors) {
        googleMap?.let {
            val marker = it.addMarker(getMarkerOptions(location, userFriend, markerColors))
            addNewMarkerToMapAndRemoveOld(userFriend, marker)
            zoomToUserLocation(location)
        }
    }

    private fun addNewMarkerToMapAndRemoveOld(userFriend: UserFriendDTO, marker: Marker?) {
        if (marker != null) {
            userMarkerMap[userFriend]?.remove()
            userMarkerMap[userFriend] = marker
        }
    }

    override fun openSettingsFragment() {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrame, SettingsFragment())
        fragmentTransaction.addToBackStack("SettingsFragment")
        fragmentTransaction.commit()
    }
}
