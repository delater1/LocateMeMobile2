package fk.com.locatememobile.app.data.databaseAccessObjects

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.entities.UserFriend
import fk.com.locatememobile.app.data.rest.dtos.UserFriendDTO
import io.reactivex.Flowable

/**
 * Created by korpa on 26.11.2017.
 */
@Dao
interface UserFriendsDAO {

    @Query("SELECT u.id AS userId," +
            " uf.id AS userFriendId," +
            " u.device AS device," +
            " u.manufacturer AS manufacturer," +
            " u.token AS token," +
            " uf.alias AS alias" +
            " FROM Users u JOIN UserFriends uf ON u.id = uf.userFriendId" +
            " WHERE uf.userId = (SELECT id FROM Users WHERE token = :token)")
    fun getUserFriendsDtosForToken(token: String): Flowable<List<UserFriendDTO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserFriend(userFriend: UserFriend)

    @Query("DELETE FROM UserFriends")
    fun deleteAllUserFriends()

    @Query("SELECT * FROM UserFriends")
    fun getAllUserFriends(): List<UserFriend>
}