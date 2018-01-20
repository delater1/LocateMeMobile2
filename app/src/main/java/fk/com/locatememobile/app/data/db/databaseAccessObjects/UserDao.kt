package fk.com.locatememobile.app.data.db.databaseAccessObjects

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import fk.com.locatememobile.app.data.entities.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM Users")
    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: List<User>)

    @Query("DELETE FROM Users")
    fun deleteAllUsers()

    @Query("SELECT * FROM Users WHERE token = :token")
    fun getUserByToken(token:String ) : User
}