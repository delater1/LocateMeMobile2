package fk.com.locatemedata.data

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import fk.com.locatemedata.data.databaseAccessObjects.LocationDAO
import fk.com.locatemedata.data.databaseAccessObjects.UserDAO
import fk.com.locatemedata.data.entities.Location
import fk.com.locatemedata.data.entities.User


/**
 * Created by korpalsk on 2017-10-12.
 */
@Database(entities = arrayOf(User::class, Location::class), version = 1, exportSchema = false)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun locationDao(): LocationDAO
}