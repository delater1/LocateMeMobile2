package fk.com.locatememobile.app.data.databaseAccessObjects

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import fk.com.locatememobile.app.data.entities.UserFriend

/**
 * Created by korpa on 26.11.2017.
 */
@Dao
interface UserFriendsDAO {

    @Query("SELECT * FROM UserFriend WHERE userId = :userId")
    fun getUserFriends(userId: Long): List<UserFriend>

    @Insert
    fun insertUserFriends(userFriends: List<UserFriend>)
}