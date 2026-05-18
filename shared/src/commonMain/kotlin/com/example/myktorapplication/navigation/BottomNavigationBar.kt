package com.example.myktorapplication.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    NavigationBar(
        // set background color
        containerColor = Color.White,
        modifier = Modifier.drawBehind {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = -30f,
                    endY = size.height
                ),
                topLeft = Offset(0f, -30f)
            )
        },) {
        // observe the backstack
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        // Bottom nav items
        BottomNavItems.forEach { navItem ->

            NavigationBarItem(
                // it currentRoute is equal then its selected route
                selected = currentRoute == navItem.destination,
                // navigate on click
                onClick = {
                    navController.navigate(navItem.destination)
                },
                // Icon of navItem
                icon = {
                    Icon(imageVector = navItem.tabIcon, contentDescription = navItem.tabName, modifier = Modifier.size(30.dp))
                },
                // label
                label = {
                    Text(text = navItem.tabName)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black, // Icon color when selected
                    unselectedIconColor = Color.Gray, // Icon color when not selected
                    selectedTextColor = Color.Black, // Label color when selected
                    indicatorColor = Color.White // Highlight color for selected item
                ),
            )
        }
    }
}