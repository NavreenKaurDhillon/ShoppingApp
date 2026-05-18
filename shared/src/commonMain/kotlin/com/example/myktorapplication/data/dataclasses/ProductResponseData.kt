package com.example.myktorapplication.data.dataclasses

data class ProductResponseData(
    val limit: Int?,
    val products: List<Product>,
    val skip: Int?,
    val total: Int?
)