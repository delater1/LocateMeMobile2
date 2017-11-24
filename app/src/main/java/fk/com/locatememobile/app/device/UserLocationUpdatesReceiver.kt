package fk.com.locatememobile.app.device

import fk.com.locatememobile.app.data.entities.Location

/**
 * Created by korpa on 21.11.2017.
 */
interface UserLocationUpdatesReceiver {
    fun onUserLocationUpdate(location: Location)
}