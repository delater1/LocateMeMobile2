package fk.com.locatememobile.app.data.rest.services

import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.entities.UserFriend
import fk.com.locatememobile.app.data.rest.dtos.UserFriendDTO
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserFriendsService {

    @GET("/user/{userId}/friends")
    fun getUserFriends(@Path("userId") userId: Long): Single<List<User>>

    @POST("/user/{userId}/friends")
    fun postUserFriend(@Path("userId") userId: Long, @Body userFriendDTO: UserFriendDTO): Single<UserFriend>
}