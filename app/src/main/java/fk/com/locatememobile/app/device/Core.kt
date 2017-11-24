package fk.com.locatememobile.app.device

import android.content.Context
import fk.com.locatememobile.app.data.Repository
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by korpa on 20.11.2017.
 */
class Core : LocationSubscriptionStateListener, UserLocationUpdatesReceiver {
    var applicationContext: Context
    var repository: Repository
    var loggedInUser: User? = null

    @Inject
    constructor(applicationContext: Context, repository: Repository) {
        this.applicationContext = applicationContext
        this.repository = repository
    }

    fun isUserAvailable() = loggedInUser != null

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
        return Completable.create { e: CompletableEmitter ->
            repository.createNewUser(firstName, lastName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    { user: User ->
                        loggedInUser = user
                        e.onComplete()
                    },
                    {
                        error: Throwable -> e.onError(error)
                    }
            )
        }
    }
}