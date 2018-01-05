package fk.com.locatememobile.app.ui

import android.util.Log
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.rest.dtos.UserFriendDTO
import fk.com.locatememobile.app.device.Core
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by korpa on 06.11.2017.
 */
class MapFragmentPresenter : MapFragmentContract.Presenter {
    @Inject
    constructor(core: Core) {
        this.core = core
        friendColorPairs = listOf()
    }

    val TAG = javaClass.simpleName

    val core: Core
    var view: MapFragmentContract.View? = null
    var friendColorPairs: List<Pair<UserFriendDTO, MarkerColors>>
    var isFirstLocationUpdate = true
    override fun register(view: MapFragmentContract.View) {
        this.view = view
        view.setToken(core.getUserToken())
        getUserFriendsDtos()
        subscribeToLocationUpdates()
    }

    override fun viewResumed() {
        refreshUserFriends()
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
}
