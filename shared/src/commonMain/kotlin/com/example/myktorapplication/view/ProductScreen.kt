package com.example.myktorapplication.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myktorapplication.intent.ProductIntent
import com.example.myktorapplication.navigation.Screens
import com.example.myktorapplication.utils.BOLD_STYLE
import com.example.myktorapplication.utils.CustomCommonImage
import com.example.myktorapplication.viewmodel.ProductViewModel
import myktorapplication.shared.generated.resources.Res
import myktorapplication.shared.generated.resources.filter_icon
import myktorapplication.shared.generated.resources.sort_icon
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductScreen(navController: NavController) {
    val viewModel = koinViewModel<ProductViewModel>()
    val products by viewModel.state.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
    var activeSortOption by remember { mutableStateOf("Relevance") }
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        SortFilterDialog(
            activeSortOption,
            onDismiss = { showDialog = false },
            onSortSelected = { option ->
                activeSortOption = option
                viewModel.handleIntent(ProductIntent.SortProducts(option))
                showDialog = false
            })
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            SortFilterBottomBar(
                onFilterClick = { showDialog = true })
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "Products List", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Spacer(Modifier.height(20.dp))
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (products.isLoading) {
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
                        items(products.products) { product ->
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
                                    text = "Price: " + product.price,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    fontWeight = FontWeight.SemiBold,
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


    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun SortFilterDialog(
        activeSortOption : String,
        onDismiss: () -> Unit,
        onSortSelected: (String) -> Unit,
    ) {
        // List of sorting options
        val sortOptions = listOf("Relevance", "Price Low to High", "Price High to Low", "Rating High to Low")
        var selectedOption by remember { mutableStateOf(activeSortOption) }
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Sort Options") },
            containerColor = Color.White,
            text = {
                Column {
                    Text("Sort By:", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        sortOptions.forEach { option ->
                            FilterChip(
                                selected = selectedOption == option,
                                onClick = {
                                    selectedOption = option
                                },
                                label = { Text(option) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color.White,   // Unselected background color
                                    selectedContainerColor = Color.Black,  // Selected background color
                                    labelColor = Color.Black,   // Unselected text color
                                    selectedLabelColor = Color.White   //Selected text color
                                ))

                        }
                    }
                    // Separator
                    HorizontalDivider(Modifier.padding(vertical = 12.dp))
                }
            },
            confirmButton = {
                Button(onClick = {
                    onSortSelected(selectedOption)
                },  colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White,
                )) {
                    Text("APPLY")
                }
            },
            dismissButton = {

                TextButton(onClick = onDismiss) {
                    Text("CANCEL")
                }
            },
            tonalElevation = 15.dp,
            modifier = Modifier.shadow(15.dp, shape = AlertDialogDefaults.shape)

        )
    }

@Composable
fun SortFilterBottomBar(
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Button to open the dialog
        TextButton(onClick = onFilterClick, modifier = Modifier.weight(0.5f)) {
            Image(
                painter = painterResource(Res.drawable.sort_icon),
                contentDescription = "Sort and Filter",
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(5.dp))
            Text("SORT", style = BOLD_STYLE)
        }
        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 15.dp)
                .width(1.dp) // thickness
                .height(30.dp) //  height for the line
                .background(Color.LightGray) //  color
        )
        TextButton(onClick = onFilterClick, modifier = Modifier.weight(0.5f)) {
            Image(
                painter =  painterResource(Res.drawable.filter_icon),
                contentDescription = "Sort and Filter",
                modifier = Modifier.size(20.dp)

            )
            Spacer(Modifier.width(5.dp))
            Text("FILTER", style = BOLD_STYLE)
        }
    }
//    }
}

