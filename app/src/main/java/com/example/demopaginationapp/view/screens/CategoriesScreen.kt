package com.example.demopaginationapp.view.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.demopaginationapp.R
import com.example.demopaginationapp.intents.CategoryUiState
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.NORMAL_STYLE
import com.example.demopaginationapp.utils.RounderRecGlideImage
import com.example.demopaginationapp.utils.SMALL_BOLD_STYLE
import com.example.demopaginationapp.viewmodel.CategoryViewmodel

@Composable
fun CategoriesScreen() {

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val viewmodel: CategoryViewmodel = hiltViewModel(viewModelStoreOwner = activity)
//    val resource by viewmodel.categoriesList.observeAsState(initial = Resource.loading(null))
    val categoryState by viewmodel.categoryState.collectAsStateWithLifecycle()
    val subCategoryState by viewmodel.subCategoryState.collectAsStateWithLifecycle()
//    val subCategoriesResource by viewmodel.subCategoriesList.observeAsState(initial = Resource.loading(null))
//    val subCategoriesState = subCategoriesResource

    when  {
        categoryState.isLoading -> {
              Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() }
        }
        categoryState.error!=null -> {
            // Show the error message
            Text(text = "Failed to load products: ${categoryState.error}", color = Color.Red, modifier = Modifier.padding(16.dp))
        }
        else -> {
            val data = categoryState.categories
            ShowCategories(
                categoriesState = categoryState, subCategoryState
                , viewmodel = viewmodel)
            LaunchedEffect(Unit) { viewmodel.getSubCategories(data[0].prodcat_id.toInt()) }
        }
    }
}


@Composable
fun ShowCategories(
    categoriesState: CategoryUiState,
    subCategoriesState: CategoryUiState,
    viewmodel: CategoryViewmodel
) {
    var selectedId by remember { mutableStateOf(categoriesState.categories[0].prodcat_id) }
    Column(Modifier.fillMaxSize()) {
        Text(text = "Shopping Categories", style = BOLD_STYLE, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 20.sp)
        Spacer(Modifier.height(20.dp))
        Row(modifier = Modifier.background(color = Color.White).fillMaxWidth()) {
            LazyColumn(modifier = Modifier.weight(0.25f).background(color = colorResource(R.color.light_blue))
                .padding(start = 12.dp).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                items(categoriesState.categories.size) { pos ->
                    val item =categoriesState.categories
                    Column(modifier = Modifier.fillMaxWidth().background(
                                color = if (categoriesState.categories[pos].prodcat_id == selectedId) Color.White else colorResource(
                                    R.color.light_blue))
                            .clickable {
                                selectedId = categoriesState.categories[pos].prodcat_id
                                viewmodel.getSubCategories(
                                    categoriesState.categories[pos].prodcat_id.toInt()
                                )
                            },
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(Modifier.height(8.dp))
                        RounderRecGlideImage(item[pos].icon, 70.dp, isSquare = false)
                        Text(text = item[pos].prodcat_name, style = SMALL_BOLD_STYLE, modifier = Modifier.padding(vertical = 10.dp))
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(0.75f) // Matches the remaining layout balance
                    .fillMaxHeight()
            ) {
                when {
                    subCategoriesState.isLoading -> {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator() }
                    }

                    subCategoriesState.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No products found!",
                                style = NORMAL_STYLE,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    else -> {
                        if (subCategoriesState.categories.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "No products found!",
                                    style = NORMAL_STYLE,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                                    .padding(start = 10.dp, end = 10.dp, bottom = 15.dp),
                                verticalArrangement = Arrangement.spacedBy(15.dp),
                            ) {
                                items(subCategoriesState.categories.size) { pos ->
                                    val item = subCategoriesState.categories

                                    Column(horizontalAlignment = Alignment.Start) {
                                        ElevatedButton(
                                            onClick = {},
                                            contentPadding = PaddingValues(
                                                vertical = 2.dp,
                                                horizontal = 10.dp
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                                .background(color = Color.White)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = item[pos].prodcat_name,
                                                    style = BOLD_STYLE
                                                )
                                                Spacer(Modifier.fillMaxWidth().weight(1f))
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                                    contentDescription = "next icon",
                                                    tint = Color.Black
                                                )
                                            }
                                        }
                                        FlowRow(maxItemsInEachRow = 3) {
                                            item[pos].children.forEach {
                                                Column(
                                                    modifier = Modifier.fillMaxWidth(0.3f)
                                                        .padding(vertical = 10.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    RounderRecGlideImage(
                                                        it.icon,
                                                        70.dp,
                                                        true,
                                                        isSquare = false
                                                    )
                                                    Spacer(Modifier.height(8.dp))
                                                    Text(
                                                        text = it.prodcat_name,
                                                        style = SMALL_BOLD_STYLE,
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}