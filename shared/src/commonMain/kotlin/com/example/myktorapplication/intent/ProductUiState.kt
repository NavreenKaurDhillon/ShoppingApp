package com.example.myktorapplication.intent

import com.example.myktorapplication.model.dataclasses.Product

data class ProductUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val baseProducts: List<Product> = emptyList(),
    val error: String? = null
)