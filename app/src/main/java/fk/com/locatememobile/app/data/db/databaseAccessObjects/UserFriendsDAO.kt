package fk.com.locatememobile.app.data.db.databaseAccessObjects

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import fk.com.locatememobile.app.data.entities.UserFriend
import fk.com.locatememobile.app.data.rest.dtos.UserFriendDTO
import io.reactivex.Flowable

@Dao
interface UserFriendsDAO {

    @Query("SELECT u.id AS userId," +
            " uf.userFriendId AS userFriendId," +
            " ufo.device AS device," +
            " ufo.manufacturer AS manufacturer," +
            " ufo.token AS token," +
            " uf.alias AS alias" +
            " FROM Users u " +
            "JOIN UserFriends uf ON u.id = uf.userId " +
            "JOIN Users ufo ON uf.userFriendId = ufo.id "  +
            "WHERE u.token = :token")
    fun getUserFriendsDtosForToken(token: String): Flowable<List<UserFriendDTO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserFriend(userFriend: UserFriend)

    @Query("DELETE FROM UserFriends")
    fun deleteAllUserFriends()

    @Query("SELECT * FROM UserFriends")
    fun getAllUserFriends(): List<UserFriend>
}