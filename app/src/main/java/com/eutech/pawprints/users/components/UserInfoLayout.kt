package com.eutech.pawprints.users.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.home.presentation.HomeEvents
import com.eutech.pawprints.shared.data.users.Users
import com.eutech.pawprints.users.UserState
import com.eutech.pawprints.users.UsersEvents
import kotlinx.coroutines.launch
import kotlin.math.tan


@Composable
fun UserInfoLayout(
    modifier: Modifier = Modifier,
    users: Users,
    state: UserState,
    events: (UsersEvents) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 5 }, initialPage = 0)
    val scope = rememberCoroutineScope()
    val tabs = listOf("Basic Info", "Pets" ,"Inbox","Appointments","Transactions")
    Column(
        modifier = modifier.fillMaxSize().padding(12.dp),
    ) {

        UserListItem(users = users)
        TabRow(selectedTabIndex = pagerState.currentPage, modifier = modifier.fillMaxWidth()) {
            tabs.forEachIndexed { index, tabName ->
                val isSelected = pagerState.currentPage == index
                Tab(
                    selected = isSelected,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                        if (index == 1) {
                          //  onGetPetAppointments()
                        }
                    },
                    text = {
                        Text(tabName)
                    }
                )
            }
        }
        HorizontalPager(
            modifier = modifier.fillMaxSize(),
            state = pagerState,
            userScrollEnabled = false,
        ) { index ->
            Text(tabs[index])
        }
    }
}