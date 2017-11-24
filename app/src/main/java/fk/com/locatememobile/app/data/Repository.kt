package fk.com.locatememobile.app.data

import android.util.Log
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter

/**
 * Created by korpa on 06.11.2017.
 */
class Repository(val serverRepository: ServerRepository, val roomDatabase: RoomDatabase) {
    val TAG = javaClass.simpleName
    fun postLocation(location: Location) {
        serverRepository.locationEndpoint.postUserLocation(location)
        roomDatabase.locationDao().insert(location)
    }

    fun createNewUser(firstName: String, lastName: String): Single<User> {
        return Single.create { e: SingleEmitter<User> ->
            serverRepository.userEndpoint.createNewUser(User(0, firstName, lastName)).subscribe(
                    { user: User ->
                        roomDatabase.userDao().insert(user)
                        e.onSuccess(user)
                    },
                    { error: Throwable ->
                        Log.d(TAG, error.message)
                        e.onError(error)
                    }
            )
        }
    }

    fun getLocationsInLast5min(userId: Long) {
        //TODO
    }

    fun getLocationSubscription(userId: Long): Observable<List<Location>> {
        return serverRepository.locationEndpoint.getLocationsSubscrition(userId)
    }
}