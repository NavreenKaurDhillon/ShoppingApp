package com.example.demopaginationapp.intents

import com.example.demopaginationapp.model.dataclasses.Product

sealed class ProductIntent {

    object LoadProducts : ProductIntent()  //fetch products list

    data class SortProducts(val option: String) : ProductIntent()  //sort the list

//    data class AddToCart(val product: Product) : ProductIntent()  // any other intent

}