package fk.com.locatememobile.app.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Locations")
@ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])
data class Location(
        @PrimaryKey(autoGenerate = true)
        var id: Long,
        var userId: Long,
        var time: Long,
        var latitude: Double,
        var longitude: Double,
        var accuracy: Float)