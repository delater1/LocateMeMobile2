package fk.com.locatememobile.app.ui.contracts

import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.rest.dtos.UserFriendDTO
import fk.com.locatememobile.app.ui.MarkerColors

interface MapFragmentContract {
    interface View {
        fun setToken(token: String)
        fun zoomToUserLocation(location: Location)
        fun showUserFriends(userFriendsAndAliases: List<Pair<UserFriendDTO, MarkerColors>>)
        fun showUserFriendsLocations(userFriendsLocationsInBuckets: Array<List<Location?>>, usersMarkerPairs: List<Pair<UserFriendDTO, MarkerColors>>)
        fun showTimeSeekBarView()
        fun displaySelectedUserFriendLocation(selectedUserFriend: UserFriendDTO, location: Location?, markerColors: MarkerColors)
        fun openSettingsFragment()
    }

    interface Presenter {
        fun register(view: View)
        fun viewResumed()
        fun userSelected(user: UserFriendDTO)
        fun userSelectionCancelled()
        fun onSeekBarValueChanged(progress: Int)
        fun onRefresh()
        fun onSettingsClicked()
    }
}