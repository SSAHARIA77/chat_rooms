package com.saharia.chatroom

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.saharia.chatroom.data.Screen
import com.saharia.chatroom.screen.ChatRooms
import com.saharia.chatroom.screen.ChatScreen
import com.saharia.chatroom.screen.LoginScreen
import com.saharia.chatroom.screen.RegistrationScreen
import com.saharia.chatroom.ui.theme.ChatRoomTheme
import com.saharia.chatroom.viewmodel.AuthViewModel
import com.saharia.chatroom.viewmodel.RoomViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatRoomTheme {
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val roomViewModel: RoomViewModel = viewModel()
    val startDestination = if(FirebaseAuth.getInstance().currentUser != null) Screen.ChatRooms.route else Screen.Login.route



    NavHost(
        startDestination = startDestination,
        navController = navController
    ) {
        composable(Screen.Registration.route){
            RegistrationScreen(goToLoginPage = {
                navController.navigate(Screen.Login.route){
                    popUpTo(Screen.Registration.route){inclusive = true}
                }
            }, authViewModel)
        }
        composable(Screen.Login.route) {
            LoginScreen(goToRegistrationScreen = {
                navController.navigate(Screen.Registration.route){
                    popUpTo(Screen.Login.route){inclusive = true}
                }
            }, authViewModel){
                navController.navigate(Screen.ChatRooms.route){
                    popUpTo(Screen.Login.route){
                        inclusive = true
                    }
                }
            }
        }

        composable("${Screen.ChatScreen.route}/{roomId}"){
            val roomId: String = it
                .arguments?.getString("roomId") ?: ""
            ChatScreen(roomId = roomId){
                navController.navigate(Screen.ChatRooms.route){
                    popUpTo(Screen.ChatScreen.route){
                        inclusive = true
                    }
                }
            }
        }


        composable(Screen.ChatRooms.route) {
            ChatRooms(roomViewModel,onRoomJoinClicked = {
                    room->
                val roomId = room.id
                if(roomId.isNotEmpty()){
                    navController.navigate("${Screen.ChatScreen.route}/${room.id}")
                }else{
                    Log.e("Navigation Error", "Room id null!!!!")
                }
            },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route){
                        popUpTo(Screen.ChatRooms.route){
                            inclusive = true
                        }
                    }
                })
        }
    }
}


