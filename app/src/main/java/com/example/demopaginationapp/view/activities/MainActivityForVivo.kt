package com.example.demopaginationapp.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.compose.rememberNavController
import com.example.demopaginationapp.navigation.AppNavHostSetup
import com.example.demopaginationapp.navigation.Screens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityForVivo : ComponentActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val navController = rememberNavController()

            AppNavHostSetup(
                navController = navController,
                padding = PaddingValues(),
                startDestination = Screens.VivoLogin
            )
        }
    }
}