package com.example.myktorapplication.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myktorapplication.navigation.Screens.route
import com.example.myktorapplication.screens.AccountScreen
import com.example.myktorapplication.screens.ProductDetailScreen
import com.example.myktorapplication.screens.ProductScreen
@Composable
fun NavHostSetup(
    navController: NavHostController,
    padding: PaddingValues,
    ) {

    NavHost(
        modifier = Modifier.background(color = Color.White),
        navController = navController,
        startDestination = Screens.Product  //set the start destination - the first visible default fragment
    ) {
        composable(Screens.Product.route) {
            ProductScreen(navController = navController)
        }
        composable(Screens.AccountScreen) {
            AccountScreen(navController = navController)
        }
        composable(
            route = Screens.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            ProductDetailScreen(productId = productId, navController = navController)
        }
    }
}