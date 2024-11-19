package com.eutech.pawprints.pets.components

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.presentation.appointment.AppointmentCard
import com.eutech.pawprints.appointments.presentation.appointment.PetAppointmentCard
import com.eutech.pawprints.pets.PetState
import com.eutech.pawprints.shared.data.pets.Pet
import kotlinx.coroutines.launch


@Composable
fun PetInfo(
    modifier: Modifier = Modifier,
    selectedPet : Pet,
    state : PetState,
    onGetPetAppointments : () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
    val scope = rememberCoroutineScope()
    val tabs = listOf("Info", "Appointments")
    LaunchedEffect(pagerState) {

    }
    Column(
        modifier = modifier.fillMaxSize().padding(8.dp)
    ){
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            PetCardInfo(pet = selectedPet)
        }
       TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, tabName ->
                val isSelected = pagerState.currentPage == index
                Tab(
                    selected = isSelected,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                        if (index == 1) {
                            onGetPetAppointments()
                        }
                    },
                    text = {
                        Text(tabName)
                    }
                )
            }
        }
        HorizontalPager(state = pagerState, userScrollEnabled = false) {
            when(it) {
                0 -> PetDetails(pet = selectedPet)
                1 -> PetAppointmentsLayout(state = state)
            }
        }
    }
}





