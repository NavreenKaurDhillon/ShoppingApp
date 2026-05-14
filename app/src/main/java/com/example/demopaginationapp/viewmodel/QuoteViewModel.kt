package com.example.demopaginationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demopaginationapp.model.dataclasses.ProductResponseData
import com.example.demopaginationapp.model.dataclasses.ResponseList
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuoteViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {
    private var rawList = MutableLiveData<Resource<ResponseList>>()
    var itemsList: LiveData<Resource<ResponseList>> = rawList


     fun getList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = appRepository.getList()
            rawList.postValue(response)
        }
    }


}