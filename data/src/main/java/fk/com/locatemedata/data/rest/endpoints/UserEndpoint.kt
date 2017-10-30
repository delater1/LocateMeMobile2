package fk.com.locatemedata.data.rest.endpoints

import fk.com.locatemedata.data.rest.services.UserService
import retrofit2.Retrofit

/**
 * Created by korpa on 21.10.2017.
 */
class UserEndpoint(retrofit: Retrofit, val userService: UserService = retrofit.create(UserService::class.java)) {

    fun getUserById(id: Long) =
                userService.getUserById(id)


    fun getUsers() =
            userService.getUsers()
}