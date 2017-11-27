package fk.com.locatememobile.app.data.databaseAccessObjects

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.entities.UserFriend
import io.reactivex.Flowable

/**
 * Created by korpa on 26.11.2017.
 */
@Dao
interface UserFriendsDAO {

    @Query("SELECT * FROM Users WHERE id IN (SELECT userFriendId FROM UserFriend WHERE userId = :userId)")
    fun getUserFriends(userId: Long): Flowable<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserFriends(userFriends: List<UserFriend>)

    @Query("DELETE FROM UserFriend")
    fun deleteAllUserFriends()

    @Query("SELECT * FROM UserFriend")
    fun getAllUserFriends(): List<UserFriend>
}