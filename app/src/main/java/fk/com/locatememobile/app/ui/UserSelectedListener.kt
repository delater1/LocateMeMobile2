package fk.com.locatememobile.app.ui

import fk.com.locatememobile.app.data.entities.User

/**
 * Created by korpa on 27.11.2017.
 */
interface UserSelectedListener {
    fun onUserSelected(user: User)
}