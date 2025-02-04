package com.saharia.chatroom.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.saharia.chatroom.data.Injection
import com.saharia.chatroom.data.Message
import com.saharia.chatroom.data.MessageRepository
import com.saharia.chatroom.data.Result
import com.saharia.chatroom.data.User
import com.saharia.chatroom.data.UserRepository
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {
    private val messageRepository: MessageRepository = MessageRepository(Injection.instance())
    private val userRepository: UserRepository =
        UserRepository(FirebaseAuth.getInstance(), Injection.instance())

    init {
        loadCurrentUser()
    }

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _roomId = MutableLiveData<String>()
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    // Load the current user from UserRepository
    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is Result.Success -> {
                    _currentUser.value = result.data
                }
                is Result.Error -> {

                }

                is Result.Logout->{

                }
            }
        }
    }

    // Load messages for the current room
    fun loadMessages() {
        viewModelScope.launch {
            val roomId = _roomId.value
            if (roomId != null) {
                messageRepository.getChatMessages(roomId).collect { messages ->
                    _messages.value = messages
                }
            } else {
                // Log error or handle null case
            }
        }
    }

    // Send a new message
    fun sendMessage(text: String) {
        val currentUser = _currentUser.value
            ?: // Log error and return early to avoid crash
            return

        val message = Message(
            senderFirstName = currentUser.firstName,
            senderId = currentUser.email,
            text = text
        )

        viewModelScope.launch {
            when (messageRepository.sendMessage(_roomId.value.toString(), message)) {
                is Result.Success -> {
                    // Handle success
                }
                is Result.Error -> {
                    // Handle error
                }

                is Result.Logout ->{

                }
            }
        }
    }

    // Set the current room ID and load its messages
    fun setRoomId(roomId: String) {
        _roomId.value = roomId
        loadMessages()
    }
}
