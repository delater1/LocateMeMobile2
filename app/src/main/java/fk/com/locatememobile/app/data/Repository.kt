package fk.com.locatememobile.app.data

import fk.com.locatememobile.app.data.entities.Location
import io.reactivex.Observable

/**
 * Created by korpa on 06.11.2017.
 */
class Repository(val serverRepository: ServerRepository, val roomDatabase: RoomDatabase) {
    fun postLocation(location: Location) {
        serverRepository.locationEndpoint.postUserLocation(location)
        roomDatabase.locationDao().insert(location)
    }

    fun getLocationsInLast5min(userId: Long){
        //TODO
    }

    fun getLocationSubscription(userId: Long): Observable<List<Location>> {
        return serverRepository.locationEndpoint.getLocationsSubscrition(userId)
    }
}