package com.saharia.chatroom.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saharia.chatroom.R
import com.saharia.chatroom.data.Room
import com.saharia.chatroom.ui.theme.AppBar
import com.saharia.chatroom.viewmodel.RoomViewModel

@Composable
fun ChatRooms(
    roomViewModel: RoomViewModel,
    onRoomJoinClicked: (Room)->Unit,
    onLogout: ()->Unit
){
    val showAddRoomDialog = remember { mutableStateOf(false) }
    val newRoomName = remember{ mutableStateOf("") }
    val rooms by roomViewModel.rooms.observeAsState(emptyList())


    Scaffold(
        topBar = { AppBar("ChatRooms", onLogout )}
    ){
        Column (
            modifier = Modifier.fillMaxSize()
                .padding(it)
                .background(colorResource(R.color.lightYellow)),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            LazyColumn(
                modifier = Modifier.background(colorResource(R.color.lightYellow))
            ){
                items(rooms){
                    room->
                    RoomItem(room, onRoomJoinClicked)
                }
            }

            Button(
                modifier = Modifier.padding(it),
                onClick = {showAddRoomDialog.value = true},
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.butterscotch))
            ) {
                Text("Create Room",
                    color = colorResource(R.color.raisinBlack),
                    style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold))
            }

//            Button(
//                modifier = Modifier.padding(it),
//                onClick = {
//                    onLogout()
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.butterscotch))
//            ) {
//                Text("Logout",
//                    color = colorResource(R.color.raisinBlack),
//                    style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold))
//            }

        }
        AddRoomDialog(showAddRoomDialog, newRoomName, roomViewModel)

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoomDialog(show: MutableState<Boolean>, roomName: MutableState<String>, roomViewModel: RoomViewModel){
    if(show.value){
        BasicAlertDialog(
            onDismissRequest = {show.value = false},
            modifier = Modifier.wrapContentSize()
                .clip(RoundedCornerShape(32.dp))
        ) {
            Column (
                modifier = Modifier.wrapContentSize()
                    .background(color = colorResource(R.color.lightYellow))
                    .padding(16.dp)
            ){
                Text("Create a Room",
                    modifier = Modifier.padding(16.dp),
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    ),
                    )
                OutlinedTextField(
                    value = roomName.value,
                    onValueChange = {roomName.value = it},
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.butterscotch)
                    ),
                    label = {Text("Room Name")},
                    modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    Button(
                        modifier = Modifier.padding(bottom = 16.dp),
                        onClick = {
                            if(roomName.value.isNotEmpty()){
                                roomViewModel.createRoom(roomName.value)
                                roomViewModel.loadRooms()
                                show.value = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.butterscotch))
                    ){
                        Text("Add",
                            color = colorResource(R.color.raisinBlack),
                            style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold))
                    }

                    Button(
                        modifier = Modifier.padding(bottom = 16.dp),
                        onClick = {show.value = false},
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.butterscotch))
                    ){
                        Text("Cancel",
                            color = colorResource(R.color.raisinBlack),
                            style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        )
                    }


                }
            }
        }
    }

}



@Composable
fun RoomItem(room: Room, onRoomJoinClicked: (Room) -> Unit){

    Card (
        modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
            .clickable { onRoomJoinClicked(room) }
        ,
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.butterscotch))
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = room.name,
                modifier = Modifier.padding(16.dp),
                color = colorResource(R.color.raisinBlack),
                style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun RoomItemPrev(){
    RoomItem(Room(name = "My Room")){}
}