package com.eutech.pawprints.ui.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import java.math.BigInteger
import java.security.MessageDigest


// Function to get MD5 hash from email
fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}

// Jetpack Compose function to display email avatar
@Composable
fun EmailAvatar(email: String, modifier: Modifier = Modifier, size: Int = 100) {
    val hash = md5(email.trim().lowercase())
    val url = "http://profiles.google.com/s2/photos/profile/" + hash + "?sz=" + size;

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .placeholder(android.R.drawable.ic_menu_report_image)
            .build()
    )

    Box(modifier = modifier.size(size.dp)) {
        // Display the avatar image
        Image(
            painter = painter,
            contentDescription = "Avatar for $email",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(size.dp)
        )

        // Optional: Show loading indicator
        if (painter.state is coil.compose.AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                color = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

//
//@Composable
//fun AvatarPhoto(imageUrl: String, modifier: Modifier = Modifier) {
//    AsyncImage(
//        model = imageUrl,
//        modifier = modifier.size(40.dp).clip(CircleShape).background(Color.White).border(2.dp, Color.White, CircleShape),
//        contentScale = ContentScale.Crop,
//        contentDescription = "Avatar",
//    )
//}
//
//Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
//    Row(horizontalArrangement = Arrangement.spacedBy((-12).dp), verticalAlignment = Alignment.CenterVertically) {
//        AvatarPhoto(modifier = Modifier.size(32.dp), imageUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?fit=crop&w=512&q=80")
//        AvatarPhoto(modifier = Modifier.zIndex(25f).size(40.dp), imageUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?fit=crop&w=512&q=80")
//        AvatarPhoto(modifier = Modifier.zIndex(50f).size(48.dp), imageUrl = "https://images.unsplash.com/photo-1567532939604-b6b5b0db2604?fit=crop&w=512&q=80")
//        AvatarPhoto(modifier = Modifier.zIndex(25f).size(40.dp), imageUrl = "https://images.unsplash.com/photo-1540569014015-19a7be504e3a?fit=crop&w=512&q=80")
//        AvatarPhoto(modifier = Modifier.size(32.dp), imageUrl = "https://images.unsplash.com/photo-1506863530036-1efeddceb993?fit=crop&w=512&q=80")
//    }
//
//    Row(horizontalArrangement = Arrangement.spacedBy((-12).dp), verticalAlignment = Alignment.CenterVertically) {
//        AvatarPhoto(imageUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?fit=crop&w=512&q=80")
//        AvatarPhoto(imageUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?fit=crop&w=512&q=80")
//        AvatarPhoto(imageUrl = "https://images.unsplash.com/photo-1567532939604-b6b5b0db2604?fit=crop&w=512&q=80")
//        AvatarPhoto(imageUrl = "https://images.unsplash.com/photo-1540569014015-19a7be504e3a?fit=crop&w=512&q=80")
//        AvatarPhoto(imageUrl = "https://images.unsplash.com/photo-1506863530036-1efeddceb993?fit=crop&w=512&q=80")
//    }
//}