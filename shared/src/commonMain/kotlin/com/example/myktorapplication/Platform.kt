package com.example.myktorapplication

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform