package fk.com.locatememobile.app.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by korpalsk on 2017-10-12.
 */
@Entity(tableName = "Users")
data class User(var device: String,
                var manufacturer: String) {
    @PrimaryKey
    var id: Long = -1
    var token: String = ""
}