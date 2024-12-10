package com.eutech.pawprints.ui.custom

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eutech.pawprints.R


@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    image : String,
    size  : Dp = 120.dp,
    onClick : () -> Unit
) {
    AsyncImage(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                color = Color.Gray
            ).clickable { onClick() },
        model = image,
        contentScale = ContentScale.Crop,
        contentDescription = "Profile",
        placeholder = painterResource(id = R.drawable.profile_bold),
        error = painterResource(id = R.drawable.profile_bold)
    )

}

fun String.createLog(
    message : String,
    e : Exception?,
) {
    Log.e(this,message,e)
}