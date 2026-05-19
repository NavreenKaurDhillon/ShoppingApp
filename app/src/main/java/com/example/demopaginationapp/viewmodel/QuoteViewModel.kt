package com.example.demopaginationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demopaginationapp.intents.ProductUiState
import com.example.demopaginationapp.intents.QuoteIntent
import com.example.demopaginationapp.intents.QuoteUiState
import com.example.demopaginationapp.model.dataclasses.ProductResponseData
import com.example.demopaginationapp.model.dataclasses.ResponseList
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
class QuoteViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {
   /* private var rawList = MutableLiveData<Resource<ResponseList>>()
    var itemsList: LiveData<Resource<ResponseList>> = rawList*/

    private val _state = MutableStateFlow(QuoteUiState())
    val state: StateFlow<QuoteUiState> = _state

    init {
        handleIntent(QuoteIntent.LoadProducts)
    }

    fun handleIntent(quoteIntent: QuoteIntent){
        when(quoteIntent){
            is QuoteIntent.LoadProducts -> getList()
        }
    }

     fun getList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = appRepository.getList()
            when(response.status){
                Status.SUCCESS -> {
                    val products = response.data ?: emptyList()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            products = products,
                            baseProducts = products
                        )
                    }
                }
                Status.ERROR -> {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            error = response.message
                        )
                    }
                }
                Status.LOADING -> {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                }
                Status.NO_INTERNET -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
//            rawList.postValue(response)
        }
    }


}