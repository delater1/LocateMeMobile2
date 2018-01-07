package fk.com.locatememobile.app.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by korpa on 16.10.2017.
 */
@Entity(tableName = "Locations")
@ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userId"))
data class Location(
        @PrimaryKey(autoGenerate = true)
        var id: Long,
        var userId: Long,
        var time: Long,
        var latitude: Double,
        var longitude: Double,
        var accuracy: Float)