package fk.com.locatememobile.app.ui

import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import io.reactivex.Flowable
import io.reactivex.Observable


/**
 * Created by korpa on 26.11.2017.
 */

interface MapFragmentContract {
    interface View {
        fun onUserFriendsListReceived(userFriendsRes: List<User>)
        fun displayUpdatedUserLocations(userFriendsLocationMap: Map<User, Location>)
    }

    interface Presenter {
        fun register(view: View)
        fun onViewReady()
        fun getLocationObservable(): Observable<Location>
        fun getUserFriends(): List<User>
        fun getLastUserLocation(user: User): Location?
        fun getUsersColors(): List<Pair<User, MarkerColors>>
        fun getUserMarkerColor(user: User): MarkerColors
        fun clear()
    }
}