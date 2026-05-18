package com.example.myktorapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myktorapplication.data.dataclasses.Product
import com.example.myktorapplication.navigation.Screens
import com.example.myktorapplication.network.ApiService
import com.example.myktorapplication.utils.CustomCommonImage

import androidx.compose.runtime.collectAsState
import com.example.myktorapplication.viewmodel.ProductViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductScreen(navController: NavController) {
    val viewModel = koinViewModel<ProductViewModel>()
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()



    Scaffold() { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding).padding(horizontal = 20.dp)
        ) {
            Text(text = "Products List", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {

                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier.fillMaxWidth(),
                        verticalItemSpacing = 15.dp, // Spacing between items vertically
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                    ) {
                        items(products) { product ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color.Black,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(10.dp)
                                    .clickable {
                                        navController.navigate("${Screens.ProductDetail}/${product.id}")
                                    }
                            ) {
                                CustomCommonImage(
                                    image = product.image,
                                    widthDp = 0.dp,
                                    heightDp = 200.dp,
                                    paddingDp = 8.dp,
                                    elevationDp = 4.dp,
                                    imageShape = RoundedCornerShape(12.dp)
                                )

                                Text(
                                    text = product.title,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = Color.White,
                                )

                                Text(
                                    text = product.description,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = Color.White,
                                    maxLines = 3
                                )
                            }
                        }
                    }

                    /*                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth().fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(products.size) {  index ->

                        Column(modifier = Modifier.fillMaxSize()
                            .background(color = Color.Black, shape = RoundedCornerShape(12.dp)).padding(10.dp)) {
                            CustomCommonImage(
                                image = products[index].image,
                                widthDp = 0.dp,
                                heightDp = 200.dp,
                                paddingDp = 8.dp,
                                elevationDp = 4.dp,
                                imageShape = RoundedCornerShape(12.dp) // Uniform roundness on all corners
                            )
                            Text(text = products[index].title, modifier = Modifier.padding(vertical = 8.dp), color = Color.White, maxLines = 2, minLines = 2)
                            Text(text = products[index].description, modifier = Modifier.fillMaxSize().padding(vertical = 8.dp), minLines = 3, maxLines = 3, color = Color.White)
                            Spacer(modifier = Modifier.weight(1f))

                        }
                    }
                }*/
                }
            }
        }
    }

}