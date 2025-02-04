package com.saharia.chatroom.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.saharia.chatroom.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, onExit: ()->Unit){
    TopAppBar(
        title = {
            Text(title,
                fontWeight = FontWeight.W500,
                color = colorResource(R.color.raisinBlack)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.butterscotch)),
        navigationIcon = {
            IconButton(
                onClick = {
                    onExit()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ExitToApp,
                    contentDescription = null
                )
            }
        }
    )
}