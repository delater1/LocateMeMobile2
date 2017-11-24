package fk.com.locatememobile.app.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by korpalsk on 2017-10-12.
 */
@Entity(tableName = "Users")
data class User(@PrimaryKey var id: Long,
                var firstName: String,
                var lastName: String)