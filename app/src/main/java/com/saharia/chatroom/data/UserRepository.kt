package com.saharia.chatroom.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class UserRepository(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
){
    suspend fun signUp(email: String, password: String, firstName: String, lastName: String): Result<Boolean>{
        try{
            auth.createUserWithEmailAndPassword(email, password).await()
            saveUserToFireStore(User(email = email, firstName = firstName, lastName = lastName))
            return Result.Success(true)
        }catch (e: Exception){
            return Result.Error(e)
        }
    }

    private suspend fun saveUserToFireStore(user: User){
        fireStore.collection("users").document(user.email).set(user).await()
    }

    suspend fun login(email: String, password: String): Result<Boolean>{
        try{
            auth.signInWithEmailAndPassword(email, password).await()
            return Result.Success(true)
        }catch(e: Exception){
            return Result.Error(e)
        }
    }

    fun logout(): Result<Boolean>{
        try{
            auth.signOut()
            return Result.Logout(true)
        }catch(e: Exception){
            return Result.Success(true)
        }
    }

    suspend fun getCurrentUser(): Result<User> {
        return try {
            // Check if the user is authenticated
            val currentFirebaseUser = auth.currentUser
            if (currentFirebaseUser != null) {
                val userDocument = fireStore.collection("users").document(currentFirebaseUser.email!!).get().await()
                if (userDocument.exists()) {
                    val user = userDocument.toObject(User::class.java)
                    if (user != null) {
                        Result.Success(user)
                    } else {
                        Result.Error(Exception("User data is null"))
                    }
                } else {
                    Result.Error(Exception("User not found in FireStore"))
                }
            } else {
                Result.Error(Exception("No user is currently logged in"))
            }
        } catch (e: Exception) {
            Result.Error(e) // Return any errors that occur during the process
        }
    }


}