package fk.com.locatememobile.app.data.rest.services

import fk.com.locatememobile.app.data.entities.User
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by korpa on 21.10.2017.
 */
interface UserFriendsService {

    @GET("/user/{userId}/friends")
    fun getUserFriends(@Path("userId") userId: Long): Single<List<User>>

    @POST("/user/{userId}/friends")
    fun postUserFriends(@Path("userId") userId: Long, @Body userFriends: List<User>): Completable
}