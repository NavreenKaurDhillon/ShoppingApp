package com.example.myktorapplication.intent

sealed class ProductIntent {
    object LoadProducts : ProductIntent()
    data class SortProducts(val option : String) : ProductIntent()
}