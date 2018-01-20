package fk.com.locatememobile.app.data

import android.util.Log
import fk.com.locatememobile.app.data.db.RoomDatabase
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.entities.UserFriend
import fk.com.locatememobile.app.data.rest.ServerRepository
import fk.com.locatememobile.app.data.rest.dtos.UserFriendDTO
import io.reactivex.*
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

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

    fun getUserFriendsLocationsInBuckets(token: String): Observable<Array<List<Location?>>> {
        return Observable.create { emitter: ObservableEmitter<Array<List<Location?>>> ->
            roomDatabase.userFriendsDao().getUserFriendsDtosForToken(token).subscribe(
                    { userFriends: List<UserFriendDTO> ->
                        if (userFriends.isNotEmpty()) {
                            Single.zip(getLocationSingleForEveryUser(userFriends),
                                    Function<Array<Any>, Array<List<Location?>>> { t: Array<Any> ->
                                        t.map { it as List<Location?> }.toTypedArray()
                                    }).subscribe(
                                    { userFriendsLocations: Array<List<Location?>> ->
                                        emitter.onNext(userFriendsLocations)
                                        emitter.onComplete()
                                    },
                                    { error: Throwable -> emitter.onError(error) })
                        } else {
                            emitter.onComplete()
                        }
                    }
                    ,
                    { error: Throwable -> emitter.onError(error) }
            )
        }
    }

    private fun getLocationSingleForEveryUser(userFriends: List<UserFriendDTO>): List<Single<List<Location>>> =
            userFriends.map { serverRepository.locationEndpoint.getBucketedUserLocationsInLast24H(it.userFriendId) }

    fun addUserFriend(user: User, friendToken: String, friendAlias: String): Completable {
        val userFriendDTO = UserFriendDTO(token = friendToken, alias = friendAlias)
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