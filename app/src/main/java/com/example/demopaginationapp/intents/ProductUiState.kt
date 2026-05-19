package com.example.demopaginationapp.intents

import com.example.demopaginationapp.model.dataclasses.Product

data class ProductUiState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val baseProducts: List<Product> = emptyList(),
    val cartProducts: List<Product> = emptyList(),
    val error: String? = null
)