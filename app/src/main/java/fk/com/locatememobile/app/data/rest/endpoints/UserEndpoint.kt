package fk.com.locatememobile.app.data.rest.endpoints

import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.rest.services.UserService

/**
 * Created by korpa on 21.10.2017.
 */
class UserEndpoint(val userService: UserService) {

    fun getUserById(id: Long) =
                userService.getUserById(id)


    fun getUsers() =
            userService.getUsers()

    fun createNewUser(user: User) =
            userService.addUser(user)
}