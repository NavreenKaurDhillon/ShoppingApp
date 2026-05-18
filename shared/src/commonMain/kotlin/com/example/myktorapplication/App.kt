package com.example.myktorapplication

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myktorapplication.navigation.BottomNavigationBar
import com.example.myktorapplication.navigation.NavHostSetup
import com.example.myktorapplication.navigation.Screens
import com.example.myktorapplication.navigation.bottomBarRoutes
import com.example.myktorapplication.screens.ProductDetailScreen
import com.example.myktorapplication.screens.ProductScreen
import org.koin.compose.KoinContext

@Composable
fun App() {
    KoinContext {
        MaterialTheme {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            Scaffold(
                bottomBar = {
                    if (currentRoute in bottomBarRoutes)
                        BottomNavigationBar(navController = navController)
                },
            ) { paddingValues ->
                NavHostSetup(navController = navController, padding = paddingValues)
            }
        }
    }
}