package com.example.myktorapplication.navigation

object Screens {
    const val Product = "product_screen"
    const val ProductDetail = "product_detail_screen"
    const val AccountScreen = "account_screen"

    val String.route: String
        get() = if (this == ProductDetail) "$this/{productId}" else this
}

val bottomBarRoutes = listOf(Screens.Product, Screens.AccountScreen)