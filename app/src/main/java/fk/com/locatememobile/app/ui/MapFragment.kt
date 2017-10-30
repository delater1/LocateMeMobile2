package fk.com.locatememobile.app.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment

import fk.locateme.app.R


class MapFragment : Fragment() {
    val TAG = this::class.java.simpleName
    var googleMap: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_map, container, false)
        return view
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val m = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        m.getMapAsync({ map ->
//            bindToLocationService()
            googleMap = map
        })
    }

//    private fun bindToLocationService() {
//        val intent = Intent(activity, LocationResultService::class.java)
//        activity.bindService(intent, object : ServiceConnection {
//            override fun onServiceDisconnected(p0: ComponentName?) {
//                Log.d(TAG, "Service Disconnected")
//            }
//
//            override fun onServiceConnected(componentName: ComponentName, Binder: IBinder) {
//                Log.d(TAG, "Service Connected")
//                if (Binder is LocationResultService.LocationServiceBinder) {
//                    Binder.getLocationObservable()?.subscribe(object : Observer<LocationResult> {
//                        override fun onError(e: Throwable) {
//                        }
//
//                        override fun onComplete() {
//                        }
//
//                        override fun onSubscribe(d: Disposable) {
//                        }
//
//                        override fun onNext(t: LocationResult) {
//                            Log.d(TAG, "update ${t.lastLocation.longitude}")
//                            googleMap?.addMarker(MarkerOptions().position(LatLng(t.lastLocation.latitude, t.lastLocation.longitude)))
//                            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(t.lastLocation.latitude, t.lastLocation.longitude),13.0f))
//                        }
//                    })
//                }
//            }
//        }, Context.BIND_AUTO_CREATE)
//    }


}
