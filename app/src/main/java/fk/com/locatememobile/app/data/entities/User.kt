package fk.com.locatememobile.app.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Users")
data class User(var device: String,
                var manufacturer: String) {
    @PrimaryKey
    var id: Long = -1
    var token: String = ""
}