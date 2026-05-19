package com.example.myktorapplication.model.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating2
)

@Serializable
data class Rating2(
    val rate: Double,
    val count: Int
)