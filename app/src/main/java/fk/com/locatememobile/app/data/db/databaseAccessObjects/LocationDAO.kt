package fk.com.locatememobile.app.data.db.databaseAccessObjects

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import fk.com.locatememobile.app.data.entities.Location

@Dao
interface LocationDAO {
    @Query("SELECT * FROM Locations")
    fun getAll(): List<Location>

    @Insert
    fun insert(location: Location)

    @Query("DELETE FROM Locations")
    fun deleteAllLocations()
}