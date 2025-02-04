package com.saharia.chatroom.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RoomRepository (private val firebaseFireStore: FirebaseFirestore){

    suspend fun createRoom(name: String): Result<Unit>{
        try{
            val room = Room(name = name)
            firebaseFireStore.collection("rooms").add(room).await()
            return Result.Success(Unit)
        }catch(e: Exception){
            return Result.Error(e)
        }
    }

    suspend fun getRooms(): Result<List<Room>>{
        try{
            val querySnapshot = firebaseFireStore.collection("rooms").get().await()
            val rooms = querySnapshot.documents.map {
                documentSnapshot->
                documentSnapshot.toObject(Room::class.java)!!.copy(id = documentSnapshot.id)
            }

            return Result.Success(rooms)


        }catch(e: Exception){
            return Result.Error(e)
        }
    }


}