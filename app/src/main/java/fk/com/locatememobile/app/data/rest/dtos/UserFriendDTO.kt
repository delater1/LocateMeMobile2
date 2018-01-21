package fk.com.locatememobile.app.data.rest.dtos

data class UserFriendDTO(
        val userId: Long = -1,
        val userFriendId: Long = -1,
        val manufacturer: String = "",
        val device: String = "",
        val token: String,
        val alias: String)