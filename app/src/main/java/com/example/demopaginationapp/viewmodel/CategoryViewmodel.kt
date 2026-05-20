package com.example.demopaginationapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demopaginationapp.di.CategoriesApi
import com.example.demopaginationapp.intents.CategoryUiState
import com.example.demopaginationapp.model.dataclasses.CategoriesResponseData
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.networking.Status
import com.example.demopaginationapp.model.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class CategoryViewmodel @Inject constructor(@CategoriesApi private val appRepository: AppRepository) :
    ViewModel() {


   /* val _categoriesList = MutableLiveData<Resource<CategoriesResponseData>>()
    val categoriesList: LiveData<Resource<CategoriesResponseData>> = _categoriesList


    val _subCategoriesList = MutableLiveData<Resource<CategoriesResponseData>>()
    val subCategoriesList: LiveData<Resource<CategoriesResponseData>> = _subCategoriesList*/

    val _categoryState = MutableStateFlow(CategoryUiState())
    val categoryState  :StateFlow<CategoryUiState> = _categoryState

    private val _subCategoryState = MutableStateFlow(CategoryUiState())
    val subCategoryState: StateFlow<CategoryUiState> = _subCategoryState



    init {
        getCategories()  //placed here so it fetch the data first time and keeps it in case of screen orientation
        //no multiple api calls due to recomposition
    }

    fun getCategories(parentId: Int = 0) {
        _categoryState.value = _categoryState.value.copy(
            isLoading = true,
        )
        viewModelScope.launch(Dispatchers.IO) {
            val response = appRepository.getCategories(parentId)
            when(response.status){
                Status.SUCCESS -> {
                    val categories = response.data?.data?.categories ?: emptyList()
                    _categoryState.value = _categoryState.value.copy(
                        isLoading = false,
                        categories = categories,
                        baseCategories = categories
                    )
                }
                Status.ERROR -> {
                    _categoryState.value = _categoryState.value.copy(isLoading = true, error = response.message)
                }
                Status.LOADING -> {
                    _categoryState.value = _categoryState.value.copy(isLoading = true)
                }
                Status.NO_INTERNET -> {
                    _categoryState.value = _categoryState.value.copy(isLoading = false)
                }
            }
//            _categoriesList.postValue(response)
        }
    }

    fun getSubCategories(catId: Int = 0) {
        _subCategoryState.value = _categoryState.value.copy(
            isLoading = true,
        )
        if (catId != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = appRepository.getCategories(catId)
                when(response.status){
                    Status.SUCCESS -> {
                        val categories = response.data?.data?.categories ?: emptyList()
                        _subCategoryState.value = _subCategoryState.value.copy(
                            isLoading = false,
                            categories = categories,
                            baseCategories = categories
                        )
                    }
                    Status.ERROR -> {
                        _subCategoryState.value = _subCategoryState.value.copy(isLoading = true, error = response.message)
                    }
                    Status.LOADING -> {
                        _subCategoryState.value = _subCategoryState.value.copy(isLoading = true)
                    }
                    Status.NO_INTERNET -> {
                        _subCategoryState.value = _subCategoryState.value.copy(isLoading = false)
                    }
                }
//                _subCategoriesList.postValue(response)
            }
        }
    }


}