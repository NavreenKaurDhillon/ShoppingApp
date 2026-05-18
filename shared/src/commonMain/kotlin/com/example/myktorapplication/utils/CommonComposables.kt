package com.example.myktorapplication.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun CustomCommonImage(
    image: String,
    widthDp: Dp,
    heightDp: Dp,
    paddingDp: Dp,
    elevationDp: Dp,
    imageShape: RoundedCornerShape
) {
    val layoutModifier = if (widthDp > 0.dp) {
        Modifier.size(width = widthDp, height = heightDp)
    } else {
        Modifier.fillMaxWidth().height(heightDp)
    }
    val finalModifier = layoutModifier
        .padding(paddingDp)
        .shadow(elevation = elevationDp, shape = imageShape)
        .background(Color.White, shape = imageShape)
        .clip(imageShape)

    AsyncImage(
        model = image,
        contentDescription = "img",
        modifier = finalModifier,
        contentScale = ContentScale.Fit
    )
}
