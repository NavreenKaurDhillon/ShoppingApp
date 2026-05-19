package com.example.demopaginationapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demopaginationapp.di.ProductApi
import com.example.demopaginationapp.intents.ProductIntent
import com.example.demopaginationapp.intents.ProductUiState
import com.example.demopaginationapp.model.dataclasses.Product
import com.example.demopaginationapp.model.dataclasses.ProductResponseData
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.networking.Status
import com.example.demopaginationapp.model.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(@ProductApi private val appRepository: AppRepository) : ViewModel() {

   /*  //trigger ui updates
    private var rawProducts = MutableLiveData<Resource<ProductResponseData>>()
    private var baseProducts = MutableLiveData<Resource<ProductResponseData>>()
    var products: LiveData<Resource<ProductResponseData>> = rawProducts*/
   var cartProducts =  mutableStateListOf<Product>()
    private val _state = MutableStateFlow(ProductUiState())
    val state: StateFlow<ProductUiState> = _state


    init {
//        getProducts()
           handleIntent(ProductIntent.LoadProducts)

    }

    fun handleIntent(productIntent: ProductIntent){
        when(productIntent){
            is ProductIntent.LoadProducts -> getProducts()
            is ProductIntent.SortProducts -> setSortOption(productIntent.option)
//            is ProductIntent.AddToCart ->  addToCart(productIntent.product)
        }
    }



    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = appRepository.getProducts()
            //using state pattern
            when(response.status){
                Status.SUCCESS ->  {
                    val products = response.data?.products ?: emptyList()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            products = products,
                            baseProducts = products
                        )
                    }
                }
                Status.ERROR -> {
                    _state.update { it.copy(isLoading = false, error = response.message) }
                }
                Status.LOADING -> {
                    _state.update { it.copy(isLoading = true, error = null) }
                }
                Status.NO_INTERNET -> {
                    _state.update { it.copy(isLoading = false, error = response.message) }
                }
            }

           //old approach with livedata
//            rawProducts.postValue(response)
//            baseProducts.postValue(response)
        }
    }

    fun setSortOption(option: String) {
//        val baseData = baseProducts.value?.data?.products
        val baseData = _state.value?.baseProducts
        val sortedProducts: List<Product>? = when (option) {
            "Price Low to High" -> baseData?.sortedBy { it.price }?.toList()
            "Price High to Low" -> baseData?.sortedByDescending { it.price }
            "Rating High to Low" -> baseData?.sortedByDescending { it.rating }
            else -> baseData
        }
        //Post a NEW Resource.success()
      /*  rawProducts.postValue(
            Resource.success(
                sortedProducts?.let {
                    ProductResponseData(
                        products = it,
                        limit = baseProducts.value?.data?.limit,
                        skip = baseProducts.value?.data?.skip,
                        total = baseProducts.value?.data?.total
                    )
                }
            )
        )*/

        _state.update {
            it.copy(products = sortedProducts?:emptyList())
        }
    }

}