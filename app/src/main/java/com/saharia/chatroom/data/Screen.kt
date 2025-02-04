package com.saharia.chatroom.data

sealed class Screen(val route: String) {
    data object Registration: Screen("registration")
    data object Login: Screen("login")
    data object ChatRooms: Screen("chat_rooms")
    data object ChatScreen: Screen("chat_screen")
}