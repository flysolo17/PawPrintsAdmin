package com.eutech.pawprints.home.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.eutech.pawprints.R
import com.eutech.pawprints.shared.data.users.Users


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserCardInfo(modifier: Modifier = Modifier,users: Users) {
    val context = LocalContext.current
    ListItem(
        icon = {
            AsyncImage(
                model = users.profile,
                contentScale = ContentScale.Crop,
                contentDescription = "${users.name} profile",
                error = painterResource(R.drawable.profile_bold),
                placeholder = painterResource(R.drawable.profile_bold),
                modifier = modifier
                    .size(36.dp)
                    .background(
                        color = Color.Gray,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
            )
        },
        text = { Text(
            "${users.name}",
            style = MaterialTheme.typography.titleMedium.copy(
                color =  MaterialTheme.colorScheme.onSurface
            ))
        },
        secondaryText = {
            Text("${users.phone}",
                style = MaterialTheme.typography.labelSmall
                    .copy(
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
            )
        },
        trailing = {
            FilledIconButton(onClick = {
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${users.phone}")
                }
                ContextCompat.startActivity(context, dialIntent, null)
            }) {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = "Phone"
                )
            }
        }
    )
}