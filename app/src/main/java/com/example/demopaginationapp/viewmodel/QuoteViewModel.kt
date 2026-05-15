package com.example.demopaginationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demopaginationapp.model.dataclasses.ProductResponseData
import com.example.demopaginationapp.model.dataclasses.ResponseList
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
class QuoteViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val connectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private var rawList = MutableLiveData<Resource<ResponseList>>()
    var itemsList: LiveData<Resource<ResponseList>> = rawList

    init {
        observeNetwork()
        getList()
    }

    private fun observeNetwork() {
        connectivityObserver.observe().onEach { status ->
            if (status == ConnectivityObserver.Status.Available) {
                if (rawList.value?.status != Status.SUCCESS) {
                    getList()
                }
            }
        }.launchIn(viewModelScope)
    }


     fun getList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = appRepository.getList()
            rawList.postValue(response)
        }
    }


}