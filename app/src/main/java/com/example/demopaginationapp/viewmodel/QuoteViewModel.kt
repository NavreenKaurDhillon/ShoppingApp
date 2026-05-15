package com.example.demopaginationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // Cache the original list for filtering/searching
    private var originalList: ResponseList? = null

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
        rawList.postValue(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            val response = appRepository.getList()
            if (response.status == Status.SUCCESS) {
                originalList = response.data
            }
            rawList.postValue(response)
        }
    }

    /**
     * Filters the list based on the search query.
     * This is a great example of business logic that should be covered by unit tests.
     */
    fun searchQuote(query: String) {
        val currentData = originalList ?: return
        
        if (query.isEmpty()) {
            rawList.postValue(Resource.success(currentData))
            return
        }

        val filtered = currentData.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
        
        val filteredResponseList = ResponseList().apply { addAll(filtered) }
        rawList.postValue(Resource.success(filteredResponseList))
    }

    /**
     * Manual retry operation for the UI to trigger.
     */
    fun retry() {
        getList()
    }


}
