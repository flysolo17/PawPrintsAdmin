package com.eutech.pawprints.users.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eutech.pawprints.shared.presentation.utils.ErrorScreen
import com.eutech.pawprints.users.InboxTabState
import com.eutech.pawprints.users.components.InboxCard


@Composable
fun InboxTab(
    modifier: Modifier = Modifier,
    inboxTabState: InboxTabState
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        if (inboxTabState.isLoading) {
            item {
                LinearProgressIndicator(
                    modifier = modifier.fillMaxWidth()
                )
            }
        }
        if(inboxTabState.errors != null) {
            item {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorScreen(
                        title = inboxTabState.errors
                    ) {
                    }
                }
            }
        }

        if (inboxTabState.errors == null && inboxTabState.inboxes.isEmpty() &&
            !inboxTabState.isLoading) {
            item {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("no inbox yet!")
                }
            }
        }
        items(inboxTabState.inboxes) {
            InboxCard(inbox = it)
        }
    }
}