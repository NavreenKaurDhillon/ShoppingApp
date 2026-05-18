package com.example.myktorapplication.di

import com.example.myktorapplication.network.ApiService
import com.example.myktorapplication.viewmodel.ProductDetailViewModel
import com.example.myktorapplication.viewmodel.ProductViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val commonModule = module {
    single { ApiService() }
    viewModelOf(::ProductViewModel)
    viewModelOf(::ProductDetailViewModel)
}

// Entry point for iOS
fun initKoin() {
    initKoin { }
}

// Common entry point for both platforms
fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(commonModule)
    }
}
