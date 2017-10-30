package fk.com.locatemedata.data.rest.services

import fk.com.locatemedata.data.entities.User
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by korpa on 16.10.2017.
 */
interface UserService {

    @GET("/user/all")
    fun getUsers(): Single<Array<User>>

    @GET("/user/{id}")
    fun getUserById(@Path("id") userId: Long): Single<User>

    @POST("/user")
    fun addUser(@Body user: User): Call<User>
}