package com.example.demopaginationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demopaginationapp.di.CategoriesApi
import com.example.demopaginationapp.model.dataclasses.CategoriesResponseData
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
class CategoryViewmodel @Inject constructor(
    @CategoriesApi private val appRepository: AppRepository,
    private val connectivityObserver: NetworkConnectivityObserver
) :
    ViewModel() {


    val _categoriesList = MutableLiveData<Resource<CategoriesResponseData>>()
    val categoriesList: LiveData<Resource<CategoriesResponseData>> = _categoriesList


    val _subCategoriesList = MutableLiveData<Resource<CategoriesResponseData>>()
    val subCategoriesList: LiveData<Resource<CategoriesResponseData>> = _subCategoriesList


    init {
        observeNetwork()
        getCategories()  //placed here so it fetch the data first time and keeps it in case of screen orientation
        //no multiple api calls due to recomposition
    }

    private fun observeNetwork() {
        connectivityObserver.observe().onEach { status ->
            if (status == ConnectivityObserver.Status.Available) {
                if (_categoriesList.value?.status != Status.SUCCESS) {
                    getCategories()
                }
                // Also retry subcategories if they were attempted and failed
                if (_subCategoriesList.value != null && _subCategoriesList.value?.status != Status.SUCCESS) {
                    // This might need a saved category ID, but for now we'll just check status
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getCategories(parentId: Int = 0) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = appRepository.getCategories(parentId)
            _categoriesList.postValue(response)

        }
    }

    fun getSubCategories(catId: Int = 0) {
        if (catId != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = appRepository.getCategories(catId)
                _subCategoriesList.postValue(response)
            }
        }
    }


}