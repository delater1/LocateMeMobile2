package fk.com.locatememobile.app.device

import android.content.Context
import android.util.Log
import fk.com.locatememobile.app.data.Repository
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.rest.services.LocationDTO
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Core : LocationSubscriptionStateListener, UserLocationUpdatesReceiver {
    val TAG = javaClass.simpleName
    var applicationContext: Context
    var repository: Repository
    var loggedInUser: User? = null
    var deviceLocationSubscriber: DeviceLocationSubscriber? = null

    @Inject
    constructor(applicationContext: Context, repository: Repository) {
        this.applicationContext = applicationContext
        this.repository = repository
    }

    fun isUserLoggedIn() = loggedInUser != null

    override fun onLocationServiceStarted() {

    }

    fun subscribeToLocationService(userLocationUpdatesReceiver: UserLocationUpdatesReceiver): DeviceLocationSubscriber? {
        loggedInUser?.let {
            return DeviceLocationSubscriber(applicationContext, it, userLocationUpdatesReceiver).bindToLocationService()
        }
        return null
    }

    fun getFriendsLocationSubscription(): Observable<List<Location>> {
        loggedInUser?.let {
            return repository.getUserLocationsSubscription(it.id)
        }
        return Observable.error(Throwable("User not logged in"))
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
                        repository.deleteAllDataFromDb()
                        loggedInUser = user
                        deviceLocationSubscriber?.userLocationUpdatesReceiver = null
                            deviceLocationSubscriber = subscribeToLocationService(this)
                        Completable.fromCallable({
                            repository.roomDatabase.userDao().insert(user)
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ completableEmitter.onComplete() })
                    },
                    { error: Throwable ->
                        completableEmitter.onError(error)
                        Log.e(TAG, error.message)
                    }
            )
        }
    }

    fun getUserFriendsFromDb(): Flowable<List<User>> {
        loggedInUser?.let {
            return repository.roomDatabase.userFriendsDao().getUserFriends(it.id)
        }
        return Flowable.error({ Throwable("User not logged in") })
    }

    fun getUsers(): Single<List<User>> {
        return Single.create { singleEmitter: SingleEmitter<List<User>> ->
            repository.serverRepository.userEndpoint.getUsers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { userList: List<User> ->
                                val userListWithoutLoggedUser = userList.toMutableList()
                                userListWithoutLoggedUser.remove(loggedInUser)
                                Observable.just(repository)
                                        .subscribeOn(Schedulers.io())
                                        .subscribe { repository ->
                                            repository.roomDatabase.userDao().insert(userList)
                                            singleEmitter.onSuccess(userListWithoutLoggedUser)
                                        }
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
                                        .subscribe { repository ->
                                            singleEmitter.onSuccess(userList)
                                        }
                            },
                            { error: Throwable ->
                                singleEmitter.onError(error)
                                Log.e(TAG, error.message)
                            }
                    )
        }
    }


    private fun getUserFriendsSingle(): Single<List<User>> {
        loggedInUser?.let {
            return repository.serverRepository.userFriendsEndpoint.getUserFriends(it)
        }
        return Single.create({ e: SingleEmitter<List<User>> -> e.onError(Throwable("User is not logged in")) })
    }

    fun postNewFriendsUserList(selectedUserFriends: List<User>?): Completable {
        selectedUserFriends?.let {
            loggedInUser?.let {
                return repository
                        .addNewUserFriends(it, selectedUserFriends)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
        return Completable.error(Throwable("Failed to post user friends list"))
    }
}