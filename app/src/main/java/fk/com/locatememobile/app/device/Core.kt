package fk.com.locatememobile.app.device

import android.content.Context
import android.util.Log
import fk.com.locatememobile.app.data.Repository
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.entities.UserFriend
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by korpa on 20.11.2017.
 */
class Core : LocationSubscriptionStateListener, UserLocationUpdatesReceiver {
    val TAG = javaClass.simpleName
    var applicationContext: Context
    var repository: Repository
    var loggedInUser: User? = null

    @Inject
    constructor(applicationContext: Context, repository: Repository) {
        this.applicationContext = applicationContext
        this.repository = repository
    }

    fun isUserLoggedIn() = loggedInUser != null

    override fun onLocationServiceStarted() {
        subscribeToLocationService(this)
    }

    fun subscribeToLocationService(userLocationUpdatesReceiver: UserLocationUpdatesReceiver) {
        loggedInUser?.let {
            DeviceLocationSubscriber(applicationContext, it, userLocationUpdatesReceiver).bindToLocationService()
        }
    }

    override fun onUserLocationUpdate(location: Location) {
        repository.postLocation(location)
    }

    fun logIn(firstName: String, lastName: String): Completable {
        return Completable.create { completableEmitter: CompletableEmitter ->
            repository.createNewUser(firstName, lastName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    { user: User ->
                        loggedInUser = user
                        completableEmitter.onComplete()
                    },
                    { error: Throwable ->
                        completableEmitter.onError(error)
                        Log.e(TAG, error.message)
                    }
            )
        }
    }

    fun getUsers(): Single<List<User>> {
        return Single.create { singleEmitter: SingleEmitter<List<User>> ->
            repository.serverRepository.userEndpoint.getUsers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { userList: List<User> ->
                                Observable.just(repository)
                                        .subscribeOn(Schedulers.io())
                                        .subscribe { repository -> repository.roomDatabase.userDao().insert(userList) }
                                singleEmitter.onSuccess(userList)
                            },
                            { error: Throwable ->
                                singleEmitter.onError(error)
                                Log.e(TAG, error.message)
                            }
                    )
        }
    }

    fun getUserFriends(): Single<List<User>> {
        return Single.create { singleEmitter: SingleEmitter<List<User>> ->
            getUserFriendsSingle()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { userList: List<User> ->
                                Observable.just(repository)
                                        .subscribeOn(Schedulers.io())
                                        .subscribe { repository -> repository.roomDatabase.userFriendsDao().insertUserFriends(convertToUserFriendsRoomList(loggedInUser, userList)) }
                                singleEmitter.onSuccess(userList)
                            },
                            { error: Throwable ->
                                singleEmitter.onError(error)
                                Log.e(TAG, error.message)
                            }
                    )
        }
    }

    private fun convertToUserFriendsRoomList(loggedInUser: User?, userList: List<User>): List<UserFriend> {
        loggedInUser?.let {
            return userList.map { user -> UserFriend(loggedInUser.id, user.id) }
        }
        return listOf()
    }

    private fun getUserFriendsSingle(): Single<List<User>> {
        loggedInUser?.let {
            return repository.serverRepository.userFriendsEndpoint.getUserFriends(it)
        }
        return Single.create({ e: SingleEmitter<List<User>> -> e.onError(Throwable("User is not logged int")) })
    }

    fun postNewFriendsUserList(selectedUserFriends: List<User>?): Completable {
        selectedUserFriends?.let {
            loggedInUser?.let {
                return repository.serverRepository.userFriendsEndpoint.postUserFriends(it, selectedUserFriends)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
        return Completable.error(Throwable("Failed to post user friends list"))
    }
}