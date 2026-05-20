package com.example.demopaginationapp.intents

import com.example.demopaginationapp.model.dataclasses.Category

data class CategoryUiState (
    val isLoading : Boolean = false,
    val categories: List<Category> = emptyList(),
    val baseCategories: List<Category> = emptyList(),
    val error: String? = null
)
