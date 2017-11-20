package fk.com.locatememobile.app.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.transitionseverywhere.Fade
import com.transitionseverywhere.Slide
import com.transitionseverywhere.TransitionManager
import fk.com.locatememobile.app.App
import fk.com.locatememobile.app.data.Repository
import fk.com.locatememobile.app.data.entities.User
import fk.locateme.app.R
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject


class MapFragment : Fragment() {
    val TAG = this::class.java.simpleName
    var googleMap: GoogleMap? = null
    @Inject
    lateinit var repository: Repository
    lateinit var model: MapFragmentViewModel
    lateinit var userAdapter: UserAdapter
    var isDrawerOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(MapFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_map, container, false)
        (activity.application as App).appComponent.inject(this)
        return view
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val m = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        m.getMapAsync({ map ->
            googleMap = map
//            setMapProperties(googleMap)
        })
        button.setOnClickListener { openCloseBottomDrawer() }
        friend_list_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        userAdapter = UserAdapter()
        friend_list_recycler_view.adapter = userAdapter
        userAdapter.addUsers(mutableListOf(User(1, "Filip", "Korpalski"), User(2, "Kalina", "Tabała")))
        Log.d(TAG, repository.toString())
    }

    private fun setMapProperties(googleMap: GoogleMap?) {
        //TODO
        googleMap?.setMaxZoomPreference(13f)
        googleMap?.setMinZoomPreference(50f)
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
}
