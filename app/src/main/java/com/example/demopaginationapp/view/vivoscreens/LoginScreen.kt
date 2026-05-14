package com.example.demopaginationapp.view.vivoscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.demopaginationapp.navigation.Screens
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.view.theme.Black50

@Composable
fun LoginScreen( navController: NavController,){
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 50.dp)) {
        Text("Enter Phone Number for Verification", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(5.dp))
        Text("This number will be used for all ride-related communication. You shall receive an SMS with code for login.", fontWeight = FontWeight.Normal,
            fontSize = 16.sp, color = Black50)
        Spacer(modifier = Modifier.height( 40.dp))
        TextField(value = "Enter Phone Number", onValueChange = {}, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height( 40.dp))
        ElevatedButton(
            onClick = {
                navController.navigate(Screens.VivoHome)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Black50, // Sets the background color of the button
                contentColor = Color.White    // Sets the color of the text/icon inside the button
            ),
        ) {
            Text(
                text = "Next",
                textAlign = TextAlign.Center,
                style = BOLD_STYLE,
                color = Color.White,
                modifier = Modifier.padding(6.dp),
                fontSize = 18.sp
            )

        }
    }
    }
