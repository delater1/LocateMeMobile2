package fk.com.locatememobile.app.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import fk.com.locatememobile.app.Constants
import fk.com.locatememobile.app.data.entities.Location

/**
 * Created by FK on 30-Dec-17.
 */
class LocationBroadcastReciver : BroadcastReceiver() {
    var locationListener: LocationUpdatesListener? = null

    override fun onReceive(p0: Context?, intent: Intent) {
        if (intent.action == Constants.IntentExtrasKeys.LOCATION_ACTION_KEY)
            locationListener?.onLocationUpdate(
                    Location(-1,
                            -1,
                            System.currentTimeMillis(),
                            intent.getDoubleExtra(Constants.IntentExtrasKeys.LATIDUTE_KEY, -1.0),
                            intent.getDoubleExtra(Constants.IntentExtrasKeys.LONGITUDE_KEY, -1.0),
                            intent.getFloatExtra(Constants.IntentExtrasKeys.ACCURACY, -1.0F)))
    }


}