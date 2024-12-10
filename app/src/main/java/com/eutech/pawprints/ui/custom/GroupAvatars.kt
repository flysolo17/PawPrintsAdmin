package com.eutech.pawprints.ui.custom

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun GroupAvatars(
    modifier: Modifier = Modifier,
    avatars: List<String>,
    avatarSize : Dp = 30.dp,
    offset : Int = 16
) {
    "avatars".createLog(avatars.toString(),null)
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        avatars.forEachIndexed { index, avatar ->
            Avatar(
                image = avatar,
                modifier = modifier
                    .offset(x = (-offset * index).dp) // Use offset for horizontal overlap
                    .zIndex(3 - index.toFloat()), // Ensures each avatar is stacked in the right order
                size = avatarSize, onClick = {}
            )
        }
    }
}

