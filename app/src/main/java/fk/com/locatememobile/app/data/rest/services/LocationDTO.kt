package fk.com.locatememobile.app.data.rest.services

import fk.com.locatememobile.app.data.entities.User

data class LocationDTO(var id: Long,
                       var user: User,
                       var time: Long,
                       var latitude: Double,
                       var longitude: Double,
                       var accuracy: Float)