package fk.com.locatememobile.app.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.transitionseverywhere.Fade
import com.transitionseverywhere.Slide
import com.transitionseverywhere.TransitionManager
import fk.com.locatememobile.app.App
import fk.com.locatememobile.app.data.Repository
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import fk.locateme.app.R
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject


class MapFragment : Fragment(), MapFragmentContract.View, UserSelectedListener {
    val TAG = this::class.java.simpleName
    var googleMap: GoogleMap? = null
    var isFirstZoom = true
    @Inject
    lateinit var repository: Repository
    @Inject
    lateinit var presenter: MapFragmentContract.Presenter
    lateinit var userAdapter: UserAdapter
    private var isDrawerOpen: Boolean = false
    lateinit var userMarkerMap: HashMap<User, Marker>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as App).appComponent.inject(this)
        presenter.register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_map, container, false)
        return view
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val m = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        userMarkerMap = hashMapOf()
        getGoogleMap(m)
        setup()
        Log.d(TAG, repository.toString())
    }

    private fun getGoogleMap(m: SupportMapFragment) {
        m.getMapAsync({ map ->
            map.clear()
            googleMap = map
            setMapProperties(googleMap)
        })
    }

    private fun animateToUserPositionIfFirstLocation(l: Location) {
        if (isFirstZoom) {
            googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(LatLng(l.latitude, l.longitude), 13f)))
            isFirstZoom = false
        }
    }

    private fun setup() {
        button.setOnClickListener { openCloseBottomDrawer() }
        friend_list_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        userAdapter = UserAdapter(this)
        friend_list_recycler_view.adapter = userAdapter
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
        constraintSet.clear(R.id.button, ConstraintSet.BOTTOM)
        constraintSet.clear(R.id.bottom_drawer, ConstraintSet.TOP)
        constraintSet.connect(R.id.bottom_drawer, ConstraintSet.BOTTOM, R.id.main_map_fragment, ConstraintSet.BOTTOM)
        constraintSet.connect(R.id.button, ConstraintSet.BOTTOM, R.id.bottom_drawer, ConstraintSet.TOP)
        TransitionManager.beginDelayedTransition(main_map_fragment, Fade(Fade.MODE_OUT))
        constraintSet.applyTo(main_map_fragment)
    }

    private fun hideBottomDrawer() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(main_map_fragment)
        constraintSet.clear(R.id.button, ConstraintSet.BOTTOM)
        constraintSet.clear(R.id.bottom_drawer, ConstraintSet.BOTTOM)
        constraintSet.connect(R.id.button, ConstraintSet.BOTTOM, R.id.main_map_fragment, ConstraintSet.BOTTOM)
        constraintSet.connect(R.id.bottom_drawer, ConstraintSet.TOP, R.id.button, ConstraintSet.BOTTOM)
        TransitionManager.beginDelayedTransition(main_map_fragment, Slide(Gravity.BOTTOM))
        constraintSet.applyTo(main_map_fragment)
    }

    private fun getMarkerOptions(location: Location, user: User): MarkerOptions? {
        return MarkerOptions()
                .position(LatLng(location.latitude, location.longitude))
                .title(user.instanceId + " " + user.device)
                .icon(BitmapDescriptorFactory.defaultMarker(presenter.getUserMarkerColor(user).markerHue))
    }

    override fun onUserSelected(user: User) {
    }

    override fun onStop() {
        super.onStop()
        googleMap?.clear()
        userMarkerMap.clear()
    }

}
