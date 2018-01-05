package fk.com.locatememobile.app.data.rest.endpoints

import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.rest.services.UserService

/**
 * Created by korpa on 21.10.2017.
 */
class UserEndpoint(private val userService: UserService) {

    fun getUserById(id: Long) =
                userService.getUserByToken(id)

    fun getUserByToken(token: String) =
            userService.getUserByToken(token)

    fun getUsers() =
            userService.getUsers()

    fun createNewUser(user: User) =
            userService.addUser(user)
}