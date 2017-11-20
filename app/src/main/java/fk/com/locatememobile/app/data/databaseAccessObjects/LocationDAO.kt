package fk.com.locatememobile.app.data.databaseAccessObjects

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import fk.com.locatememobile.app.data.entities.Location

/**
 * Created by korpa on 16.10.2017.
 */
@Dao
interface LocationDAO {
    @Query("SELECT * FROM Locations")
    fun getAll(): List<Location>

    @Insert
    fun insert(location: Location)
}