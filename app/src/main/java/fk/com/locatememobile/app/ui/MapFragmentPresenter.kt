package fk.com.locatememobile.app.ui

import android.util.Log
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.rest.dtos.UserFriendDTO
import fk.com.locatememobile.app.device.Core
import fk.com.locatememobile.app.device.LocationService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by korpa on 06.11.2017.
 */
class MapFragmentPresenter : MapFragmentContract.Presenter {
    private val TAG = javaClass.simpleName
    private val core: Core
    private var view: MapFragmentContract.View? = null
    private var friendColorPairs: List<Pair<UserFriendDTO, MarkerColors>>
    private var userFriendsLocationsInBuckets: Array<List<Location?>>
    private var isFirstLocationUpdate = true
    private var selectedUserFriend: UserFriendDTO? = null

    @Inject
    constructor(core: Core) {
        this.core = core
        friendColorPairs = listOf()
        userFriendsLocationsInBuckets = arrayOf()
    }
    override fun register(view: MapFragmentContract.View) {
        this.view = view
        view.setToken(core.getUserToken())
        getUserFriendsDtos()
        subscribeToLocationUpdates()
    }

    private fun getUserFriendsLocationsInBuckets() {
        core.getUserFriendsLocationBuckets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result: Array<List<Location?>> ->
                            userFriendsLocationsInBuckets = result
                            view?.showUserFriendsLocations(userFriendsLocationsInBuckets, friendColorPairs)
                        },
                        { error: Throwable ->
                            Log.e(TAG, error.message)
                        }
                )
    }

    override fun viewResumed() {
        refreshUserFriends()
        getUserFriendsLocationsInBuckets()
    }

    override fun userSelected(user: UserFriendDTO) {
        val lastUserLocation = getLastLocationForUser(user)
        if (lastUserLocation != null) {
            selectedUserFriend = user
            view?.zoomToUserLocation(lastUserLocation)
            view?.showTimeSeekBarView()
        }
    }

    private fun getLastLocationForUser(user: UserFriendDTO): Location? {
        return userFriendsLocationsInBuckets.find { it.find { it?.userId == user.userFriendId } != null }?.findLast { it != null }
    }

    private fun subscribeToLocationUpdates() {
        core.getLocationPublishSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { location: Location ->
                            zoomToUserLocationIfFirstLocationUpdate(location)
                        },
                        { error: Throwable -> Log.e(TAG, error.message) }
                )
    }

    private fun refreshUserFriends() {
        getUserFriendsDtos()
    }

    private fun getUserFriendsDtos() {
        core.getUserFriendsAsDtos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { userFriendsDtos: List<UserFriendDTO> ->
                            friendColorPairs = mapMarkerColorsToUserFriendDtos(userFriendsDtos)
                            view?.showUserFriends(friendColorPairs)
                        },
                        { error: Throwable ->
                            Log.e(TAG, error.message)
                        }
                )
    }


    private fun mapMarkerColorsToUserFriendDtos(userFriendDtoList: List<UserFriendDTO>): List<Pair<UserFriendDTO, MarkerColors>> {
        var index = 0
        return userFriendDtoList.map { Pair(it, getMarkerColor(index++)) }
    }

    private fun getMarkerColor(index: Int): MarkerColors {
        return MarkerColors.values()[index % MarkerColors.values().size]
    }

    private fun zoomToUserLocationIfFirstLocationUpdate(location: Location) {
        view?.let {
            if (isFirstLocationUpdate) {
                it.zoomToUserLocation(location)
                isFirstLocationUpdate = false
            }
        }
    }

    override fun userSelectionCancelled() {
        selectedUserFriend = null
    }

    override fun onSeekBarValueChanged(progress: Int) {
        selectedUserFriend?.let {
            val locations = getUserLocations(it)
            if (locations != null) {
                val size = locations.filterNotNull().size - 1
                val choosenIndex = getSelectedIndex(size, progress)
                Log.d(TAG, "Seek bar result is $choosenIndex of $size, progress: $progress")
                view?.displaySelectedUserFriendLocation(it, locations.filterNotNull()[choosenIndex], friendColorPairs.find { x -> it == x.first }!!.second)
            }
        }
    }

    private fun getSelectedIndex(size: Int, progress: Int): Int {
        return (size * progress) / 100
    }

    private fun getUserLocations(userFriend: UserFriendDTO): List<Location?>? {
        return userFriendsLocationsInBuckets.find { it.find { it?.userId == userFriend.userFriendId } != null }
    }
}

