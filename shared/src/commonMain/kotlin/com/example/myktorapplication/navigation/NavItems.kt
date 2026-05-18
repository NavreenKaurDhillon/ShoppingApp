package com.example.myktorapplication.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import com.example.myktorapplication.data.dataclasses.BottomNavItem

val BottomNavItems = listOf(

    // Products list
    BottomNavItem(
        tabName = "Product",
        tabIcon = Icons.Filled.List,
        destination = Screens.Product
    )
)