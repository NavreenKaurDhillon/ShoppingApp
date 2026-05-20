package com.example.demopaginationapp.intents

sealed class CategoryIntent {
    object LoadData : CategoryIntent()
}