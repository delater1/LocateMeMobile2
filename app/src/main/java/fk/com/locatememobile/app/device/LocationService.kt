package fk.com.locatememobile.app.device

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import fk.com.locatememobile.app.Constants.IntentExtrasKeys.ACCURACY
import fk.com.locatememobile.app.Constants.IntentExtrasKeys.LATIDUTE_KEY
import fk.com.locatememobile.app.Constants.IntentExtrasKeys.LOCATION_ACTION_KEY
import fk.com.locatememobile.app.Constants.IntentExtrasKeys.LOCATION_UPDATE_INTERVAL
import fk.com.locatememobile.app.Constants.IntentExtrasKeys.LONGITUDE_KEY

/**
 * Created by korpa on 22.10.2017.
 */
class LocationService : Service() {
    val TAG = this::class.java.simpleName
    lateinit var fusedLocationProvider: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback
    override fun onBind(p0: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        fusedLocationProvider = FusedLocationProviderClient(this)
        val interval = intent?.getLongExtra(LOCATION_UPDATE_INTERVAL, 60000) ?: 60000L
        locationCallback = createLocationCallback()
        try {
            fusedLocationProvider.requestLocationUpdates(getLocationRequest(interval), locationCallback, Looper.myLooper())
        } catch (exception: SecurityException) {
            Log.e(TAG, exception.message)
        }
        return Service.START_STICKY
    }

    private fun getLocationRequest(interval: Long): LocationRequest? {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = interval
        locationRequest.fastestInterval = interval / 2
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        return locationRequest
    }

    private fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d(TAG, "recieved location update: ${locationResult.lastLocation.latitude}, ${locationResult.lastLocation.longitude}")
                val broadcastIntent = Intent()
                broadcastIntent.action = LOCATION_ACTION_KEY
                broadcastIntent.putExtra(LATIDUTE_KEY, locationResult.lastLocation.latitude)
                broadcastIntent.putExtra(LONGITUDE_KEY, locationResult.lastLocation.longitude)
                broadcastIntent.putExtra(ACCURACY, locationResult.lastLocation.accuracy)
                sendBroadcast(broadcastIntent)
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                Log.d(TAG, "Location Availability $locationAvailability")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProvider.removeLocationUpdates(createLocationCallback())
    }
}