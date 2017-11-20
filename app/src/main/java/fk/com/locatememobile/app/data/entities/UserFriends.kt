package fk.com.locatememobile.app.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

/**
 * Created by korpa on 16.10.2017.
 */
@Entity(tableName = "UserFriends",
        foreignKeys = arrayOf(ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("id")),
                ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("id"))))
class UserFriends(var userId:Long, var userFriendId: Long)