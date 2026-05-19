package com.example.myktorapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myktorapplication.intent.ProductIntent
import com.example.myktorapplication.intent.ProductUiState
import com.example.myktorapplication.model.dataclasses.Product
import com.example.myktorapplication.model.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(private val apiService: ApiService) : ViewModel() {

   /* private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading*/

    val _state = MutableStateFlow(ProductUiState())
    val state : StateFlow<ProductUiState> = _state

    init {
        handleIntent(ProductIntent.LoadProducts)
    }

    fun handleIntent(productIntent: ProductIntent){
        when(productIntent){
            ProductIntent.LoadProducts -> fetchProducts()
            is ProductIntent.SortProducts -> sortProducts(productIntent.option)
        }
    }

    private fun fetchProducts() {
        _state.update {
            it.copy(
                isLoading = true,
            )
        }
        viewModelScope.launch {
            val response = apiService.fetchProducts()
            _state.update {
                it.copy(
                    isLoading = false,
                    products = response,
                    baseProducts = response
                )
            }
            _state.update {
                it.copy(
                    isLoading = false,
                )
            }
        }
    }


    private fun sortProducts(option: String) {
        val baseData = _state.value?.baseProducts
        val sortedProducts: List<Product>? = when (option) {
            "Price Low to High" -> baseData?.sortedBy { it.price }?.toList()
            "Price High to Low" -> baseData?.sortedByDescending { it.price }
            else -> baseData
        }
        _state.update {
            sortedProducts?.let { products ->
                it.copy(
                    isLoading = false,
                    products = products,
                )
            }!!
        }
    }
}
