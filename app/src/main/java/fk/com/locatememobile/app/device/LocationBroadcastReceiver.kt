package fk.com.locatememobile.app.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import fk.com.locatememobile.app.Constants
import fk.com.locatememobile.app.data.entities.Location
import io.reactivex.subjects.PublishSubject

class LocationBroadcastReceiver : BroadcastReceiver {
    constructor() : super() {
        publishSubject = PublishSubject.create()
    }

    val publishSubject: PublishSubject<Location>

    override fun onReceive(p0: Context?, intent: Intent) {
        if (intent.action == Constants.IntentExtrasKeys.LOCATION_ACTION_KEY) {
            publishSubject.onNext(
                    Location(0,
                            0,
                            intent.getLongExtra(Constants.IntentExtrasKeys.TIME_KEY, 0L),
                            intent.getDoubleExtra(Constants.IntentExtrasKeys.LATIDUTE_KEY, -1.0),
                            intent.getDoubleExtra(Constants.IntentExtrasKeys.LONGITUDE_KEY, -1.0),
                            intent.getFloatExtra(Constants.IntentExtrasKeys.ACCURACY, -1.0F)))
        }
    }
}
