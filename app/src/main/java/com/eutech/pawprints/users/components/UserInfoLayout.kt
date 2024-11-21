package com.eutech.pawprints.users.components

import android.provider.Telephony.Mms.Inbox
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.home.presentation.HomeEvents
import com.eutech.pawprints.shared.data.users.Users
import com.eutech.pawprints.users.UserState
import com.eutech.pawprints.users.UsersEvents
import com.eutech.pawprints.users.tabs.AppointmentsTab
import com.eutech.pawprints.users.tabs.BasicInfoTab
import com.eutech.pawprints.users.tabs.InboxTab
import com.eutech.pawprints.users.tabs.PetsTab
import com.eutech.pawprints.users.tabs.TransactionTab
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
    fun getCurrentPage(index : Int) {
        when(index) {
            1 -> users.id?.let { events(UsersEvents.OnGetPets(it)) }
            2 -> users.id?.let { events(UsersEvents.OnGetInbox(it)) }
            3 -> users.id?.let { events(UsersEvents.OnGetAppointments(it)) }
            4 -> users.id?.let { events(UsersEvents.OnGetTransactions(it)) }
        }
    }
    LaunchedEffect(
        pagerState.currentPage
    ) {
        getCurrentPage(pagerState.currentPage)
    }
    LaunchedEffect(users) {
        getCurrentPage(pagerState.currentPage)
    }


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
            when(index) {
                 0 -> BasicInfoTab(users = state.selectedUser)
                1 -> PetsTab(petTabState = state.petTabState)
                2 -> InboxTab(
                    inboxTabState = state.inboxTabState
                )
                3 -> AppointmentsTab(
                    appointmentTabState = state.appointmentTabState
                )
                4 -> TransactionTab(
                    transactionTabState = state.transactionTabState
                )
            }
        }
    }

}