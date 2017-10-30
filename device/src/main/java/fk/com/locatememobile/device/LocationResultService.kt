
package fk.com.locatememobile.device

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.google.android.gms.location.LocationResult
import io.reactivex.Observable

/**
 * Created by korpa on 22.10.2017.
 */
class LocationResultService : Service() {
    val TAG = this::class.java.simpleName

    var locationObservable: Observable<LocationResult>? = null
    lateinit var fusedLocationWrapper: FusedLocationWrapper
    override fun onBind(p0: Intent): IBinder = LocationServiceBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        fusedLocationWrapper = FusedLocationWrapper(this)
        locationObservable = Observable.create(fusedLocationWrapper).publish().autoConnect()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        fusedLocationWrapper.cancelLocationUpdates()
        locationObservable = null
    }

    inner class LocationServiceBinder : Binder() {
        fun getLocationObservable(): Observable<LocationResult>? = locationObservable
    }
}