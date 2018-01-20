package fk.com.locatememobile.app.ui

import fk.com.locatememobile.app.data.rest.dtos.UserFriendDTO

interface UserSelectedListener {
    fun onUserSelected(user: UserFriendDTO)
}