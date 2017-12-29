package fk.com.locatememobile.app.device

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

/**
 * Created by korpa on 29.10.2017.
 */
class FusedLocationWrapper(applicationContext: Context, private var fused: FusedLocationProviderClient = FusedLocationProviderClient(applicationContext)) : ObservableOnSubscribe<LocationResult> {

    private val TAG = this::class.java.simpleName
    private lateinit var observableEmitter: ObservableEmitter<LocationResult>
    lateinit var locationCallback: LocationCallback

    @SuppressLint("MissingPermission")
    override fun subscribe(observableEmitter: ObservableEmitter<LocationResult>) {
        this.observableEmitter = observableEmitter
        locationCallback = getLocationCallback(observableEmitter)
        try {
            fused.requestLocationUpdates(getLocationRequest(), locationCallback, Looper.myLooper())
        } catch (e: SecurityException) {
            Log.e(ContentValues.TAG, "Security exception on location updates")
        }
    }

    private fun getLocationRequest(): LocationRequest? {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 60000
        locationRequest.fastestInterval = 30000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        return locationRequest
    }

    private fun getLocationCallback(e: ObservableEmitter<LocationResult>): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d(TAG, "recieved location update: ${locationResult.lastLocation.latitude}, ${locationResult.lastLocation.longitude}")
                e.onNext(locationResult)
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                Log.d(TAG, "Location Availability $locationAvailability")
            }
        }
    }

    fun cancelLocationUpdates() {
        fused.removeLocationUpdates(locationCallback)
        observableEmitter.onComplete()
    }
}