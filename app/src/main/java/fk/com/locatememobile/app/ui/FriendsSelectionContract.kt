package fk.com.locatememobile.app.ui

import fk.com.locatememobile.app.data.entities.User
import io.reactivex.Single

/**
 * Created by korpa on 25.11.2017.
 */
interface FriendsSelectionContract {
    interface View {
        fun getSelectedUserFriends(): List<User>?
        fun showMapFragment()
    }

    interface Presenter {
        fun register(view: View)
        fun getUsers(): Single<List<User>>
        fun getUserFriends(): Single<List<User>>
        fun onForwardClicked()
    }
}