package fk.com.locatememobile.app.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "UserFriends",
        foreignKeys = [(ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userId"))), (ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userFriendId")))])
class UserFriend(var userId: Long, var userFriendId: Long) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var alias:String = ""
}