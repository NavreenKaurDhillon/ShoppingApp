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
     * Sorts the current list based on different criteria.
     * Demonstrates how tests can verify complex sorting logic.
     */
    fun sortQuotes(criteria: SortCriteria) {
        val currentData = rawList.value?.data ?: return
        
        val sortedList = when (criteria) {
            SortCriteria.PRICE_LOW_TO_HIGH -> currentData.sortedBy { it.price }
            SortCriteria.PRICE_HIGH_TO_LOW -> currentData.sortedByDescending { it.price }
            SortCriteria.TITLE_ASCENDING -> currentData.sortedBy { it.title }
            SortCriteria.RATING_HIGH_TO_LOW -> currentData.sortedByDescending { it.rating.rate }
        }

        val sortedResponseList = ResponseList().apply { addAll(sortedList) }
        rawList.postValue(Resource.success(sortedResponseList))
    }

    /**
     * Simulates deleting a quote from the list.
     * Useful for testing state updates.
     */
    fun deleteQuote(id: Int) {
        val currentData = rawList.value?.data ?: return
        val updatedList = currentData.filter { it.id != id }
        
        // Update both the current view and the original cache
        val newResponseList = ResponseList().apply { addAll(updatedList) }
        rawList.postValue(Resource.success(newResponseList))
        
        originalList?.let { list ->
            val cachedFiltered = list.filter { it.id != id }
            originalList = ResponseList().apply { addAll(cachedFiltered) }
        }
    }

    /**
     * Manual retry operation for the UI to trigger.
     */
    fun retry() {
        getList()
    }

}

enum class SortCriteria {
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    TITLE_ASCENDING,
    RATING_HIGH_TO_LOW
}
