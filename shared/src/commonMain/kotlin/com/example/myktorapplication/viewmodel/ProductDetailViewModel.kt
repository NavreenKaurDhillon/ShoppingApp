package com.example.myktorapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myktorapplication.data.dataclasses.Product
import com.example.myktorapplication.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(private val apiService: ApiService) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchProductDetail(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _product.value = apiService.fetchProductDetail(id)
            _isLoading.value = false
        }
    }
}
