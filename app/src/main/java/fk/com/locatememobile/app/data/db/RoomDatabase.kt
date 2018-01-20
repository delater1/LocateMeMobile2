package fk.com.locatememobile.app.data.db

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import fk.com.locatememobile.app.data.db.databaseAccessObjects.LocationDAO
import fk.com.locatememobile.app.data.db.databaseAccessObjects.UserDAO
import fk.com.locatememobile.app.data.db.databaseAccessObjects.UserFriendsDAO
import fk.com.locatememobile.app.data.entities.Location
import fk.com.locatememobile.app.data.entities.User
import fk.com.locatememobile.app.data.entities.UserFriend

@Database(entities = arrayOf(User::class, Location::class, UserFriend::class), version = 1, exportSchema = false)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun locationDao(): LocationDAO
    abstract fun userFriendsDao(): UserFriendsDAO
}