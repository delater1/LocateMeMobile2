package fk.com.locatemedata.data.rest.services

import fk.com.locatemedata.data.entities.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by korpa on 21.10.2017.
 */
interface UserFriendsService {

    @GET("/user/{userId}/friends")
    fun getUserFriends(@Path("userId") userId: Long ): Observable<List<User>>
}