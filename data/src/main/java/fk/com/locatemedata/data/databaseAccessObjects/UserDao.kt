package fk.com.locatemedata.data.databaseAccessObjects

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import fk.com.locatemedata.data.entities.User

/**
 * Created by korpalsk on 2017-10-12.
 */
@Dao
interface UserDAO {
    @Query("SELECT * FROM Users")
    fun getAll(): List<User>

    @Insert
    fun insert(user: User)
}