package fk.com.locatememobile.app.data

import android.util.Log
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.entities.UserFriend
import fk.com.locatememobile.app.data.rest.dtos.UserFriendDTO
import fk.com.locatememobile.app.data.rest.services.LocationDTO
import io.reactivex.*
import io.reactivex.functions.BiFunction
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

    fun logInUser(device: String, manufacturer: String): Single<User> {
        return Single.create { e: SingleEmitter<User> ->
            serverRepository
                    .userEndpoint
                    .createNewUser(User(device, manufacturer))
                    .subscribeOn(Schedulers.io())
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

    fun getUserLocationsSubscription(userId: Long, interval: Long): Observable<List<Location>> {
        return Observable.create<List<Location>> { observableEmitter: ObservableEmitter<List<Location>> ->
            serverRepository.locationEndpoint.getLocationDTOSubscription(userId, interval).subscribe(
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
                    locationDTO.longitude,
                    locationDTO.accuracy)
        }
    }

    fun addUserFriend(user: User, friendToken: String, friendAlias: String): Completable {
        val userFriendDTO = UserFriendDTO(token = friendToken,alias =  friendAlias)
        return Completable.create { completableEmitter: CompletableEmitter ->
            postUserFriendToServerAndSaveItInLocalDb(user, userFriendDTO, completableEmitter)
        }
    }

    private fun postUserFriendToServerAndSaveItInLocalDb(user: User, userFriendDTO: UserFriendDTO, completableEmitter: CompletableEmitter) {
        serverRepository.userFriendsEndpoint.addUserFriend(user, userFriendDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        { userFriend: UserFriend ->
                            getUserFriendFromServerAndStoreIt(userFriendDTO.token, userFriend, completableEmitter)
                        },
                        { error: Throwable -> completableEmitter.onError(error) }
                )
    }

    private fun getUserFriendFromServerAndStoreIt(friendToken: String, userFriend: UserFriend, completableEmitter: CompletableEmitter) {
        serverRepository.userEndpoint.getUserByToken(friendToken)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        { user: User ->
                            roomDatabase.userDao().insert(user)
                            roomDatabase.userFriendsDao().insertUserFriend(userFriend)
                            completableEmitter.onComplete()
                        },
                        { error: Throwable -> completableEmitter.onError(error) }
                )
    }

    fun getLoggedInUser(token: String): User {
        return roomDatabase.userDao().getUserByToken(token)
    }

    fun getUserFriendsWithAliasesFromDb(userToken: String): Flowable<List<UserFriendDTO>> {
        return roomDatabase.userFriendsDao().getUserFriendsDtosForToken(userToken)
    }
}