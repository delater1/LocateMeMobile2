package fk.com.locatememobile.app.data

import android.util.Log
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.entities.UserFriend
import fk.com.locatememobile.app.data.rest.services.LocationDTO
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by korpa on 06.11.2017.
 */
class Repository(private val serverRepository: ServerRepository,
                 private val roomDatabase: RoomDatabase) {
    val TAG = javaClass.simpleName


    fun postLocation(location: Location) {
        roomDatabase.locationDao().insert(location)
        serverRepository.locationEndpoint.postUserLocation(location)
    }

    fun logInUser(firstName: String, lastName: String): Single<User> {
        return Single.create { e: SingleEmitter<User> ->
            serverRepository
                    .userEndpoint
                    .createNewUser(User(-1, firstName, lastName))
                    .subscribe(
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

    private fun loadUsersToDb(): Completable {
        return Completable.create { e: CompletableEmitter ->
            serverRepository.userEndpoint.getUsers().subscribe(
                    { users: List<User> ->
                        roomDatabase.userDao().insert(users)
                        e.onComplete()
                    },
                    { error: Throwable -> e.onError(error) })
        }
    }

    private fun loadUserFriendsToDb(): Completable {
        return Completable.create { e: CompletableEmitter ->
        }
    }

    fun getUserLocationsSubscription(userId: Long): Observable<List<Location>> {
        return Observable.create<List<Location>> { observableEmitter: ObservableEmitter<List<Location>> ->
            serverRepository.locationEndpoint.getLocationDTOSubscription(userId).subscribe(
                    { res: List<LocationDTO> -> observableEmitter.onNext(convert(res)) },
                    { error: Throwable -> observableEmitter.onError(error) },
                    { observableEmitter.onComplete() }
            )
        }
    }

    fun convert(locationDTO: List<LocationDTO>): List<Location> {
        return locationDTO.map { locationDTO ->
            Location(locationDTO.id,
                    locationDTO.user.id,
                    locationDTO.time,
                    locationDTO.latitude,
                    locationDTO.longitude)
        }
    }

    fun addNewUserFriends(user: User, userFriends: List<User>): Completable {
        return serverRepository
                .userFriendsEndpoint.postUserFriends(user, userFriends).doOnComplete({
            roomDatabase.userFriendsDao().insertUserFriends(convertToUserFriendsRoomList(user, userFriends))
        })
    }

    private fun convertToUserFriendsRoomList(loggedInUser: User, userList: List<User>): List<UserFriend> {
        return userList.map { user -> UserFriend(loggedInUser.id, user.id) }
    }
}