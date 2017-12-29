package fk.com.locatememobile.app.data.rest.services

import fk.com.locatememobile.app.data.entities.User

/**
 * Created by korpa on 27.11.2017.
 */

data class LocationDTO(var id: Long,
                       var user: User,
                       var time: Long,
                       var latitude: Double,
                       var longitude: Double)