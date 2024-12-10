package com.eutech.pawprints.appointments.presentation.appointment

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Tab
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.AppointmentWithAttendeesAndPets
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.home.presentation.components.AppointmentCard
import com.eutech.pawprints.schedule.data.display
import com.eutech.pawprints.shared.presentation.routes.MainRouter
import com.eutech.pawprints.shared.presentation.utils.toast
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)

@Composable
fun AppointmentScreen(
    modifier: Modifier = Modifier,
    state : AppointmentState,
    events : (AppointmentEvent) -> Unit,
    navHostController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = state.selectedTab,
        pageCount = {3},
    )
    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
        if (state.isUpdated != null ) {
            context.toast(state.isUpdated)
        }
    }
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != state.selectedTab) {
            events(AppointmentEvent.OnTabSelected(pagerState.currentPage))
        }
    }
    LaunchedEffect(state.selectedTab) {
        scope.launch {
            pagerState.animateScrollToPage(state.selectedTab)
        }
    }
    val tabs = listOf(
        Pair("Pending", Icons.Default.Pending),
        Pair("Upcoming", Icons.Default.EventAvailable),
        Pair("All", Icons.Default.List)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = { navHostController.popBackStack() }
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                "Appointments", style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
        TabRow(
            selectedTabIndex = pagerState.currentPage
        ) {
            tabs.forEachIndexed { index, tab ->
                val selected = state.selectedTab == index
                Tab(
                    selected = selected,
                    onClick = {
                        events(AppointmentEvent.OnTabSelected(index))
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = tab.second,
                                contentDescription = tab.first,
                                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tab.first,
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
            }
        }
        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = modifier.fillMaxWidth()
            )
        }
        HorizontalPager(
            modifier = modifier.fillMaxSize(),
            state = pagerState
        ) {

            when(it) {
                0 -> {
                    val appointments = state.appointments.filter {
                        it.appointments.status == AppointmentStatus.PENDING
                    }
                    SelectedTab(
                        index = 0,
                        appointments = appointments,
                        state = state,
                        events = events,
                    )
                }
                1 -> {
                    val appointments = state.appointments.filter {
                        it.appointments.status == AppointmentStatus.CONFIRMED
                    }
                    SelectedTab(index = 1, appointments = appointments,    state = state,
                        events = events,)
                }
                2 -> {
                    SelectedTab(index = 2, appointments = state.appointments,    state = state,
                        events = events,)
                }
            }
        }
    }
}

@Composable
fun SelectedTab(
    modifier: Modifier = Modifier,
    index: Int,
    appointments : List<AppointmentWithAttendeesAndPets>,
    state: AppointmentState,
    events: (AppointmentEvent) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Adaptive(350.dp),
    ) {
        items(appointments) {
            AppointmentCard(
                data = it,
                isLoading = state.updatingStatus,
                onUpdateStatus = { status , appointment ->
                    events.invoke(AppointmentEvent.OnUpdateAppointment(status,appointment))
                }
            )
        }
    }
}


@Composable
fun DisplayDate(scheduleDate: String) {
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
       val formatter =   DateTimeFormatter.ofPattern("MMM, dd yyyy")
       val date = LocalDate.parse(scheduleDate, formatter)

       val day = date.dayOfMonth.toString()
       val month = date.month.getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH)
       val annotatedString = buildAnnotatedString {
           withStyle(style = SpanStyle(
               color = MaterialTheme.colorScheme.primary,
               fontSize = 18.sp,
               fontWeight = FontWeight.Black,

               )) {
               append(day)
           }
           append("\n")
           withStyle(
               style = SpanStyle(
                   fontWeight = FontWeight.Black,
               )) {
               append(month)
           }

       }

       Text(
           text = annotatedString,
           textAlign = TextAlign.Center
       )
    } else {
        Text("Invalid Date")
    }
}
@Composable
fun AppointmentTopBar(
    modifier: Modifier = Modifier,
    onCreate : () -> Unit,
    onBack : () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = {onBack}
        ) { Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "back"
        ) }
        Text(text = "Appointments", style = MaterialTheme.typography.titleLarge)


    }
}