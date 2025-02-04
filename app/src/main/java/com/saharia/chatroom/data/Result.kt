package com.saharia.chatroom.data


sealed class Result<out T> {
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val exception: Exception): Result<Nothing>()
    data class Logout<out T>(val data: T): Result<T>()
}

