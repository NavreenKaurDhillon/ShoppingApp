package com.example.demopaginationapp.intents

import com.example.demopaginationapp.model.dataclasses.ResponseListItem

data class QuoteUiState (
    val isLoading: Boolean = false,
    val products: List<ResponseListItem> = emptyList(),
    val baseProducts: List<ResponseListItem> = emptyList(),
    val error: String? = null
)