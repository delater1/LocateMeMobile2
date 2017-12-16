package fk.com.locatememobile.app.ui

import android.content.Context
import android.util.Log
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.device.Core
import fk.com.locatememobile.app.device.UserLocationUpdatesReceiver
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by korpa on 06.11.2017.
 */
class MapFragmentPresenter : UserLocationUpdatesReceiver, MapFragmentContract.Presenter {
    val TAG = javaClass.simpleName

    var view: MapFragmentContract.View? = null

    var applicationContext: Context
    var observableEmitter: ObservableEmitter<Location>? = null
    var core: Core
    var userFriendsList: MutableList<User>
    var userFriendsLocationMap: MutableMap<User, Location>
    var userFriendsColorList: MutableList<Pair<User, MarkerColors>>

    @Inject
    constructor(applicationContext: Context, core: Core) {
        this.applicationContext = applicationContext
        this.core = core
        this.userFriendsList = mutableListOf()
        this.userFriendsLocationMap = HashMap()
        this.userFriendsColorList = mutableListOf()
        core.subscribeToLocationService(this)
    }

    override fun register(view: MapFragmentContract.View) {
        this.view = view
    }

    override fun onUserLocationUpdate(location: Location) {
        observableEmitter?.onNext(location)
    }

    override fun getUsersColors(): List<Pair<User, MarkerColors>> {
        return userFriendsColorList
    }

    override fun onViewReady() {
        getUserFriendsAsync()
        subscribeToUserFriendsLocations()
    }

    override fun getLocationObservable(): Observable<Location> {
        return Observable.create<Location> { e: ObservableEmitter<Location> -> observableEmitter = e }
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }


    fun subscribeToUserFriendsLocations() {
        core.getFriendsLocationSubscription()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { userFriendsLocation: List<Location> ->
                            Log.d(TAG, "received userFriends locations update")
                            matchToLocationMap(userFriendsLocation)
                            view?.displayUpdatedUserLocations(userFriendsLocationMap.toMap())
                        },
                        { error: Throwable -> Log.e(TAG, "Error during subscribing to userFriends locations") }
                )
    }

    override fun getLastUserLocation(user: User): Location? {
        return userFriendsLocationMap[user]
    }

    private fun matchToLocationMap(userFriendsLocation: List<Location>) {
        userFriendsLocation
                .asSequence()
                .filter { location -> userFriendsList.find { it.id == location.userId } != null }
                .forEach { userFriendLocation -> userFriendsLocationMap[userFriendsList.find { it.id == userFriendLocation.userId }!!] = userFriendLocation }
    }

    private fun getUserFriendsAsync() {
        core.getUserFriendsFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { userFriendsRes: List<User> ->
                            userFriendsList = userFriendsRes.toMutableList()
                            userFriendsColorList.clear()
                            mapUsersToColors()
                            view?.onUserFriendsListReceived(userFriendsRes)
                        },
                        { error: Throwable ->
                            Log.e(TAG, error.message)
                        })
    }

    private fun mapUsersToColors() {
        var i = 0
        userFriendsList.forEach { userFriendsColorList.add(Pair(it, MarkerColors.values()[i++ % MarkerColors.values().size])) }
    }

    override fun getUserFriends(): List<User> {
        return userFriendsList
    }

    override fun getUserMarkerColor(user: User): MarkerColors {
        return userFriendsColorList.find { it.first == user }?.second ?: MarkerColors.HUE_RED
    }

    override fun clear() {
        userFriendsList.clear()
        userFriendsLocationMap.clear()
        userFriendsColorList.clear()
    }
}
