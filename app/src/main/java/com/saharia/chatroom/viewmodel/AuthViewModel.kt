package com.saharia.chatroom.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.saharia.chatroom.data.Injection
import com.saharia.chatroom.data.Result
import com.saharia.chatroom.data.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val _userRepository: UserRepository = UserRepository(
        FirebaseAuth.getInstance(),
        Injection.instance()
    )

    private val _loading = mutableStateOf(false)
    val loading: MutableState<Boolean> get() = _loading

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult



    fun signUp(email: String, password: String, firstName: String, lastName: String){
        viewModelScope.launch {
            _loading.value = true
            _authResult.value = _userRepository.signUp(email, password, firstName, lastName)
            _loading.value = false
        }

    }

    fun login(email: String, password: String){

        viewModelScope.launch {
            _loading.value = true
            _authResult.value = _userRepository.login(email, password)
            _loading.value = false
        }
    }

    fun logout(){
        viewModelScope.launch{
            _authResult.value = _userRepository.logout()
        }
    }
}