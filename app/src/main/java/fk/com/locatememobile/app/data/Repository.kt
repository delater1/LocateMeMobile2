package fk.com.locatememobile.app.data

import android.util.Log
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.entities.UserFriend
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
            serverRepository.userEndpoint.createNewUser(User(-1, firstName, lastName)).subscribe(
                    { user: User ->
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

    fun deleteAllDataFromDb() {
        Completable.fromCallable {
            roomDatabase.locationDao().deleteAllLocations()
            roomDatabase.userFriendsDao().deleteAllUserFriends()
            roomDatabase.userDao().deleteAllUsers()
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    fun addNewUserFriends(user: User, userFriends: List<User>): Completable {
        return serverRepository
                .userFriendsEndpoint.postUserFriends(user, userFriends).doOnComplete({
            roomDatabase.userFriendsDao().insertUserFriends(convertToUserFriendsRoomList(user, userFriends))
        }
        )
    }

    private fun convertToUserFriendsRoomList(loggedInUser: User, userList: List<User>): List<UserFriend> {
        return userList.map { user -> UserFriend(loggedInUser.id, user.id) }
    }
}