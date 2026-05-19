package com.example.demopaginationapp.view.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.networking.Status
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.CustomGlideImage
import com.example.demopaginationapp.viewmodel.QuoteViewModel
import kotlin.collections.get

//basic api call and fetch response
@Composable
fun QuotesListScreen() {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val viewModel: QuoteViewModel = hiltViewModel(viewModelStoreOwner = activity)
   /* viewModel.getList()
    val resource by viewModel.itemsList.observeAsState(initial = Resource.loading(null))*/

    val state by viewModel.state.collectAsStateWithLifecycle()

    when  {
        state.isLoading -> {
            Log.d("elwkhkjwehkjehw", "LOADING: resource")
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() }
        }
        state.error!=null -> {
            // Show the error message
            Text(text = "Failed to load products: ${state.error}", color = Color.Red, modifier = Modifier.padding(16.dp))
        }
        else -> {
            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                Text(text = "Shop By Brands",  fontSize = 20.sp,  modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Spacer(Modifier.height(15.dp))
                state.products?.let {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(it.size) { item ->
                            Column(modifier = Modifier.clickable{
//                                navController.navigate(Screens.Profile)
                            }.padding(bottom = 15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                CustomGlideImage(
                                    it[item].image,
                                    0.dp,
                                    8.dp,
                                    4.dp,
                                    CircleShape)
                                Text(
                                    text = it[item].title?: "Dummy", style = BOLD_STYLE,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1, fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}