package com.saharia.chatroom.screen

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.saharia.chatroom.R
import com.saharia.chatroom.data.Result
import com.saharia.chatroom.ui.theme.AppBar
import com.saharia.chatroom.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    goToRegistrationScreen: ()->Unit,
    authViewModel: AuthViewModel,
    onSignInSuccess: () -> Unit
){
    var email by remember{ mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val result by authViewModel.authResult.observeAsState()
    val context = LocalContext.current
    val activity = context as? Activity
    Scaffold (
        topBar = { AppBar("Login") { activity?.finish() } }
    ) { paddingVal ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingVal)
                .background(colorResource(R.color.lightYellow)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.padding(bottom = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.butterscotch)
                )
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.padding(bottom = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.butterscotch)
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                modifier = Modifier.width(275.dp).padding(bottom = 8.dp),
                onClick = {
                    authViewModel.login(email, password)

                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.butterscotch))
            ) {
                Text("Login",
                    color = colorResource(R.color.raisinBlack),
                    style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                )
            }

            Text(
                "New User? Register Here",
                color = colorResource(R.color.raisinBlack),
                modifier = Modifier.clickable {
                    goToRegistrationScreen()
                }
            )
        }

        if(authViewModel.loading.value){
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                CircularProgressIndicator()
            }
        }else
        LaunchedEffect(result) {
            when(result){
                is Result.Success->{
                    onSignInSuccess()
                }
                is Result.Error ->{
                    Toast.makeText(context, "User Not Found!", Toast.LENGTH_SHORT).show()
                }

                is Result.Logout->{

                }
                null->{

                }
            }
        }

    }
}
