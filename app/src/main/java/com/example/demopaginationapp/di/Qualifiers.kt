package com.example.demopaginationapp.di

import javax.inject.Qualifier

// Qualifier for Google API scope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GoogleBaseUrl
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GoogleApi

// Qualifier for Product API scope
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ProductBaseUrl
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ProductApi


// Qualifier for categories API scope
@Qualifier
@Retention (AnnotationRetention.RUNTIME)
annotation class CategoriesApi

@Qualifier
@Retention (AnnotationRetention.RUNTIME)
annotation class CategoriesBaseUrl