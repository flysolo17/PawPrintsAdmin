package com.eutech.pawprints.users.tabs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eutech.pawprints.shared.data.users.Users
import com.eutech.pawprints.shared.presentation.components.DetailRow
import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import com.eutech.pawprints.ui.theme.PawPrintsTheme
import com.eutech.pawprints.users.UserState
import com.eutech.pawprints.users.UsersEvents


@Composable
fun BasicInfoTab(
    modifier: Modifier = Modifier,
    users: Users ?,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pet Image
        AsyncImage(
            model = users?.profile,
            contentDescription = "${users?.name} image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Pet Name and Age
        Text(
            text = users?.name ?: "Unknown Name",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        DetailRow(
            label= "Phone",
            value = "${users?.phone}"

        )
        DetailRow(
            label= "Email",
            value = "${users?.email}"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BasicInfoTabPrev() {
    PawPrintsTheme {
        BasicInfoTab(
            users = Users(
                id = generateRandomNumber(),
                name = "Juan Dela Cruz",
                phone = "098898787567",
                email = "juan@gmail.com"
            )
        )
    }
}