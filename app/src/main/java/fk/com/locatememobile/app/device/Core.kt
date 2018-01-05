package fk.com.locatememobile.app.device

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import fk.com.locatememobile.app.Constants
import fk.com.locatememobile.app.data.Repository
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.rest.dtos.UserFriendDTO
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Core {
    val TAG = javaClass.simpleName
    private val applicationContext: Context
    private val repository: Repository
    private val sharedPreferencesRepository: SharedPreferencesRepository
    private var loggedInUser: User? = null
    lateinit private var locationBroadcastReceiver: LocationBroadcastReceiver

    @Inject
    constructor(applicationContext: Context, repository: Repository, sharedPreferencesRepository: SharedPreferencesRepository) {
        this.applicationContext = applicationContext
        this.repository = repository
        this.sharedPreferencesRepository = sharedPreferencesRepository
        createLocationBroadcastReceiver()
    }

    fun startLocationService() {
        applicationContext.startService(getLocationServiceIntent())
    }

    private fun createLocationBroadcastReceiver() {
        locationBroadcastReceiver = LocationBroadcastReceiver()
        applicationContext.registerReceiver(
                locationBroadcastReceiver,
                IntentFilter(Constants.IntentExtrasKeys.LOCATION_ACTION_KEY))
    }

    fun getLocationObservable(): Observable<Location> {
        return locationBroadcastReceiver.observable
    }

    fun getLocationServiceIntent(): Intent {
        val intent = Intent(applicationContext, LocationService::class.java)
        intent.putExtra(Constants.IntentExtrasKeys.LOCATION_UPDATE_INTERVAL, 60000L)
        return intent
    }

    fun logIn(): Completable {
        return Completable.create { e: CompletableEmitter ->
            if (getUserToken().isEmpty()) {
                createNewUser(e)
            } else {
                loggedInUser = repository.getLoggedInUser(getUserToken())
                e.onComplete()
            }
        }
    }

    private fun createNewUser(e: CompletableEmitter) {
        repository.logInUser(Build.MODEL, Build.MANUFACTURER)
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        { user: User ->
                            sharedPreferencesRepository.saveUserToken(user.token)
                            loggedInUser = user
                            e.onComplete()
                        },
                        { error: Throwable -> e.onError(error) }

                )
    }

    fun addUserFriend(tag: String, alias: String): Completable {
        return Completable.create { completableEmitter: CompletableEmitter ->
            repository.addUserFriend(repository.getLoggedInUser(token = getUserToken()), tag, alias)
                    .subscribe(
                            { completableEmitter.onComplete() },
                            { error: Throwable -> completableEmitter.onError(error) })
        }
    }

    fun getUserToken(): String {
        return sharedPreferencesRepository.getUserToken()
    }

    fun getUserFriendsAsDtos(): Flowable<List<UserFriendDTO>> {
        return repository.getUserFriendsWithAliasesFromDb(getUserToken())
    }
}