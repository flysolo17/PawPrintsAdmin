package com.eutech.pawprints.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Badge
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.eutech.pawprints.R
import com.eutech.pawprints.shared.data.messages.Message
import com.eutech.pawprints.shared.data.messages.UserType
import com.eutech.pawprints.shared.data.messages.UserWithMessages
import com.eutech.pawprints.shared.data.users.Users
import com.eutech.pawprints.shared.presentation.components.MessageTextField
import com.eutech.pawprints.shared.presentation.components.SearchText
import com.eutech.pawprints.shared.presentation.utils.toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    state: MessageState,
    events: (MessageEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(state.userWithMessages) {
        if (state.selectedConvo != null) {
            val convo = state.userWithMessages.firstOrNull {
                it.users?.id == state.selectedConvo.users?.id
            }
            convo?.let {
                events(MessageEvents.OnSelectConversation(it))
            }
        }
    }
    LaunchedEffect(
        state
    ) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
        if (state.messagingState.errors != null) {
            context.toast(state.messagingState.errors)
        }
        if (state.messagingState.isSent != null) {
            context.toast(state.messagingState.isSent)
        }

    }
    Row(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
                .weight(0.5f)
        ) {
            item {
                Text(
                    "Messages",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = modifier.padding(8.dp)
                )
            }
            if (state.isLoading) {
                item {
                    LinearProgressIndicator(
                        modifier.fillMaxWidth()
                    )
                }
            }
            item {
                SearchText(
                    label = "Search user here..",
                    value = state.searchText,
                    onChange = {events.invoke(MessageEvents.OnSearch(it))}
                )
            }
            items(state.filteredMessages) {
                UserWithMessagesCard(
                    it = it,
                    isSelected = state.selectedConvo?.users?.id == it.users?.id,
                    onSelectConvo = {
                        events(MessageEvents.OnSelectConversation(it))
                    }
                )
            }
        }
        VerticalDivider(
            modifier = modifier.fillMaxHeight()
        )
        Box(
            modifier = modifier.fillMaxSize().weight(1f),
            contentAlignment = Alignment.Center
        ){
            if (state.selectedConvo == null) {
                Text("No selected conversation")
            } else {
                val convo = state.selectedConvo
                ConversationLayout(
                    isLoading = state.messagingState.isLoading,
                    users = convo.users,
                    messages  = convo.messages,
                    message = state.message,
                    onChange = {events(MessageEvents.OnMessageChange(it))},
                    onSend = {
                        convo.users?.id?.let {
                            events(MessageEvents.OnSendMessage(it))
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConversationLayout(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    users: Users?,
    messages: List<Message>,
    message : String,
    onChange : (String) -> Unit,
    onSend : () -> Unit,
) {

    Column(
        modifier = modifier.fillMaxSize().padding(8.dp)
    ) {
        ListItem(
            icon = {
                AsyncImage(
                    model = users?.profile,
                    contentScale = ContentScale.Crop,
                    contentDescription = "${users?.name} profile",
                    error = painterResource(R.drawable.profile_bold),
                    placeholder = painterResource(R.drawable.profile_bold),
                    modifier = modifier
                        .size(52.dp)
                        .background(
                            color = Color.Gray,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                )
            },
            text = {
                Text("${users?.name}", style = MaterialTheme.typography.titleLarge)
            }
        )
        HorizontalDivider()
        LazyColumn(
            modifier = modifier.fillMaxSize().weight(1f).padding(8.dp),
            verticalArrangement = Arrangement.Bottom,
            reverseLayout = true
        ) {
            items(messages) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = if (it.type == UserType.ADMIN) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    ListItem(
                        modifier = modifier
                            .widthIn(max = 300.dp)
                            .background(
                                color = if (it.type == UserType.ADMIN) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                                shape = MaterialTheme.shapes.medium
                            ),
                        text = {
                            Text(
                                it.message ?: "",
                                color = if (it.type == UserType.ADMIN) MaterialTheme.colorScheme.onPrimary else  MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        },
                        secondaryText = {
                            MessageTimestamp(date = it.createdAt,)
                        }
                    )

                }
            }
        }
        HorizontalDivider()
        MessageTextField(
            isLoading =isLoading,
            message = message,
            onChange = {onChange(it)},
            onSend = {onSend()}
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserWithMessagesCard(
    modifier: Modifier = Modifier,
    it: UserWithMessages,
    isSelected : Boolean,
    onSelectConvo : () -> Unit
) {
    val user = it.users
    val messages = it.messages.sortedByDescending { it.createdAt }
    ListItem(
        modifier = modifier.fillMaxWidth()
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.1f
                ) else MaterialTheme.colorScheme.surface
            )
            .clickable { onSelectConvo() },
        icon = {
            AsyncImage(
                model = user?.profile,
                contentScale = ContentScale.Crop,
                contentDescription = "${user?.name} profile",
                error = painterResource(R.drawable.profile_bold),
                placeholder = painterResource(R.drawable.profile_bold),
                modifier = modifier
                    .size(42.dp)
                    .background(
                        color = Color.Gray,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
            )
        },
        text = {
            Row(
                modifier = modifier.fillMaxWidth()
            ) {

                Text(
                    modifier = modifier.fillMaxWidth().weight(1f),
                    text = user?.name ?: "no user",
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        },
        secondaryText = {
            val message = if (messages.isEmpty()) {
                "No Message yet"
            } else {
                messages[0].message ?: "No Message yet"
            }
            val currentMessage = messages.getOrNull(0)
            val unseenMessages = messages.filter { !it.seen && it.type == UserType.CLIENT }.size
            Row {
                Text(
                    message,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = if (unseenMessages == 0) Color.Gray else MaterialTheme.colorScheme.onSurface,
                        fontWeight =  if (unseenMessages == 0) FontWeight.Normal else FontWeight.Bold
                    ),
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (currentMessage != null) {
                    MessageTimestamp(
                        date = currentMessage.createdAt
                    )
                }
            }
        },
        trailing = {

        }
    )
}

@Composable
fun MessageTimestamp(
    modifier: Modifier = Modifier,
    date: Date
) {
    // Format date to "MMM, dd hh:mm aa"
    val formattedDate = SimpleDateFormat("MMM, dd hh:mm aa", Locale.getDefault()).format(date)

    Text(
        text = formattedDate,
        style = MaterialTheme.typography.labelSmall.copy(),
        modifier = modifier
    )
}