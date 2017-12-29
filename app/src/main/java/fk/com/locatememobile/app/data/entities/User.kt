package fk.com.locatememobile.app.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by korpalsk on 2017-10-12.
 */
@Entity(tableName = "Users")
data class User(@PrimaryKey var id: Int,
                var token: String,
                var instanceId: String,
                var device: String)