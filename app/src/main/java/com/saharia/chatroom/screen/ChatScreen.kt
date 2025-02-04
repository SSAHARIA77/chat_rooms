package com.saharia.chatroom.screen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.saharia.chatroom.R
import com.saharia.chatroom.data.Message
import com.saharia.chatroom.ui.theme.AppBar
import com.saharia.chatroom.viewmodel.MessageViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ChatScreen(
    roomId: String,
    messageViewModel: MessageViewModel = viewModel(),
    goToRooms: ()->Unit
) {

    val text = remember{mutableStateOf("")}
    val messages by messageViewModel.messages.observeAsState(emptyList())
    messageViewModel.setRoomId(roomId)

    Scaffold(
        topBar = { AppBar("Message", goToRooms) }
    ){
        paddingVal->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingVal)
        ){

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(messages) {
                    ChatMessageItem(
                        message = it.copy(
                            isSentByCurrentUser = it.senderId == messageViewModel.currentUser.value?.email
                        )
                    )
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                OutlinedTextField(
                    value = text.value,
                    onValueChange = {text.value = it},
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.butterscotch)
                    ),
                    label = { Text("Message") },
                    modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                    shape = RoundedCornerShape(10.dp)
                )

                IconButton(
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = {
                        if(text.value.isNotEmpty()){
                            messageViewModel.sendMessage(text = text.value.trim())
                            text.value = ""
                        }
                        messageViewModel.loadMessages()
                    }

                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Send,
                        contentDescription = null
                    )
                }

            }

        }
    }




}



@SuppressLint("NewApi")
@Composable
fun ChatMessageItem(message: Message){

    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if(message.senderId == FirebaseAuth.getInstance().currentUser?.email) Alignment.End else Alignment.Start
    ){
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp),
        ){

            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(8.dp),
                color = if(message.senderId == FirebaseAuth.getInstance().currentUser?.email) colorResource(R.color.butterscotch) else colorResource(R.color.cadetGray)
            ){

                Text(
                    text = message.text,
                    color = colorResource(R.color.lightYellow),
                    modifier = Modifier.padding(8.dp)
                )

            }

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message.senderFirstName,
                color = colorResource(R.color.raisinBlack),
                modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)
            )

            Text(
                text = formatTimestamp(message.timestamp),
                color = colorResource(R.color.raisinBlack),
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
            )

        }

    }

}


@RequiresApi(Build.VERSION_CODES.O)
private fun formatTimestamp(timestamp: Long): String {
    val messageDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    val now = LocalDateTime.now()

    return when {
        isSameDay(messageDateTime, now) -> "Today ${formatTime(messageDateTime)}"
        isSameDay(messageDateTime.plusDays(1), now) -> "Yesterday ${formatTime(messageDateTime)}"
        else -> formatDate(messageDateTime)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun isSameDay(dateTime1: LocalDateTime, dateTime2: LocalDateTime): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateTime1.format(formatter) == dateTime2.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return formatter.format(dateTime)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    return formatter.format(dateTime)
}




















