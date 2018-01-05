package fk.com.locatememobile.app.ui

import fk.com.locatememobile.app.data.rest.dtos.UserFriendDTO

/**
 * Created by korpa on 27.11.2017.
 */
interface UserSelectedListener {
    fun onUserSelected(user: UserFriendDTO)
}