package fk.com.locatememobile.app.data.rest.services

import fk.com.locatememobile.app.data.entities.User
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @GET("/user/token/{token}")
    fun getUserByToken(@Path("token") userToken: String): Single<User>

    @GET("/user/id/{id}")
    fun getUserByToken(@Path("id") userId: Long): Single<User>

    @POST("/user")
    fun addUser(@Body user: User): Single<User>
}