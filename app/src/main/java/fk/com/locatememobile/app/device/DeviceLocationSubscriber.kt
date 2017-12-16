package fk.com.locatememobile.app.device

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.LocationResult
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by korpa on 30.10.2017.
 */
class DeviceLocationSubscriber(val applicationContext: Context, val user: User, var userLocationUpdatesReceiver: UserLocationUpdatesReceiver?) {
    private val TAG = javaClass.simpleName

    fun bindToLocationService(): DeviceLocationSubscriber {
        val intent = Intent(applicationContext, LocationResultService::class.java)
        applicationContext.bindService(intent, object : ServiceConnection {
            override fun onServiceDisconnected(p0: ComponentName?) {
                Log.d(TAG, "Service Disconnected")
            }

            override fun onServiceConnected(componentName: ComponentName, Binder: IBinder) {
                Log.d(TAG, "Service Connected")
                if (Binder is LocationResultService.LocationServiceBinder) {
                    Binder.getLocationObservable()?.subscribe(object : Observer<LocationResult> {
                        override fun onError(e: Throwable) {
                        }

                        override fun onComplete() {
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(locationResult: LocationResult) {
                            Log.d(TAG, "update ${locationResult.lastLocation.longitude}")
                            userLocationUpdatesReceiver?.onUserLocationUpdate(convert(locationResult))
                        }
                    })
                }
            }
        }, Context.BIND_AUTO_CREATE)
        return this
    }

    private fun convert(locationResult: LocationResult): Location {
        return Location(0, user.id, locationResult.lastLocation.time, locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
    }
}