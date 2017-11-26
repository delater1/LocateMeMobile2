package fk.com.locatememobile.app.data.rest.endpoints

import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.rest.services.UserFriendsService
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by korpa on 25.11.2017.
 */
class UserFriendsEndpoint(private val userFriendsService: UserFriendsService) {

    fun getUserFriends(user: User): Single<List<User>> {
        return userFriendsService.getUserFriends(user.id)
    }

    fun postUserFriends(user: User, userFriends: List<User>): Completable {
        return userFriendsService.postUserFriends(user.id, userFriends)
    }
}