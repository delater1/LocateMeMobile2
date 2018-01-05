package fk.com.locatememobile.app.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import fk.com.locatememobile.app.Constants
import fk.com.locatememobile.app.data.entities.Location
import io.reactivex.ObservableEmitter
import io.reactivex.observables.ConnectableObservable

class LocationBroadcastReceiver : BroadcastReceiver {
    constructor() : super() {
        createConnectableObservable()
    }

    private lateinit var observableEmitter: ObservableEmitter<Location>
    lateinit var observable: ConnectableObservable<Location>

    override fun onReceive(p0: Context?, intent: Intent) {
        if (intent.action == Constants.IntentExtrasKeys.LOCATION_ACTION_KEY) {
            observableEmitter.onNext(
                    Location(-1,
                            -1,
                            System.currentTimeMillis(),
                            intent.getDoubleExtra(Constants.IntentExtrasKeys.LATIDUTE_KEY, -1.0),
                            intent.getDoubleExtra(Constants.IntentExtrasKeys.LONGITUDE_KEY, -1.0),
                            intent.getFloatExtra(Constants.IntentExtrasKeys.ACCURACY, -1.0F)))
        }
    }

    private fun createConnectableObservable() {
        observable = ConnectableObservable.create { e: ObservableEmitter<Location> ->
            observableEmitter = e
        }.publish()
        observable.connect()
    }
}