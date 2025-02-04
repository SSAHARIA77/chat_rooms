package com.saharia.chatroom.data

data class Message(
    val isSentByCurrentUser: Boolean = true,
    val text: String = "",
    val senderFirstName: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val senderId: String = ""
)
