package fk.com.locatememobile.app.device

import fk.com.locatememobile.app.data.entities.Location

/**
 * Created by FK on 30-Dec-17.
 */
interface LocationUpdatesListener {
    fun onLocationUpdate(location: Location)
}