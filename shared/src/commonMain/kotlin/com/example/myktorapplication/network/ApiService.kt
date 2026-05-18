package com.example.myktorapplication.network

import com.example.myktorapplication.data.dataclasses.Product
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class ApiService {

    // Configure the multiplatform HttpClient
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true // Prevents crashing if the API adds fields later
            })
        }
    }

    // Suspending function to make the network request
    suspend fun fetchProducts(): List<Product> {
        return try {
            httpClient.get("https://fakestoreapi.com/products").body()
        } catch (e: Exception) {
            // Handle network or parsing errors gracefully
            emptyList()
        }
    }

    // Suspending function to make the network request for a single product
    suspend fun fetchProductDetail(id: Int): Product? {
        return try {
            httpClient.get("https://fakestoreapi.com/products/$id").body()
        } catch (e: Exception) {
            // Handle network or parsing errors gracefully
            null
        }
    }


}