package com.example.demopaginationapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demopaginationapp.di.ProductApi
import com.example.demopaginationapp.model.dataclasses.Product
import com.example.demopaginationapp.model.dataclasses.ProductResponseData
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.networking.Status
import com.example.demopaginationapp.model.repositories.AppRepository
import com.example.demopaginationapp.utils.ConnectivityObserver
import com.example.demopaginationapp.utils.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    @ProductApi private val appRepository: AppRepository,
    private val connectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private var rawProducts = MutableLiveData<Resource<ProductResponseData>>()
    private var baseProducts = MutableLiveData<Resource<ProductResponseData>>()
    var products: LiveData<Resource<ProductResponseData>> = rawProducts


    init {
        observeNetwork()
        getProducts()
    }

    private fun observeNetwork() {
        connectivityObserver.observe().onEach { status ->
            if (status == ConnectivityObserver.Status.Available) {
                // If we were showing an error or just got back online, retry
                if (rawProducts.value?.status != Status.SUCCESS) {
                    getProducts()
                }
            }
        }.launchIn(viewModelScope)
    }


    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = appRepository.getProducts()
            rawProducts.postValue(response)
            baseProducts.postValue(response)
        }
    }

    fun setSortOption(option: String) {
        val baseData = baseProducts.value?.data?.products

        val sortedProducts: List<Product>? = when (option) {
            "Price Low to High" -> baseData?.sortedBy { it.price }?.toList()
            "Price High to Low" -> baseData?.sortedByDescending { it.price }
            "Rating High to Low" -> baseData?.sortedByDescending { it.rating }
            else -> baseData
        }
        //Post a NEW Resource.success()
        rawProducts.postValue(
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
        )
    }

}