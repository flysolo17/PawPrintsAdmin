package com.eutech.pawprints.pets.components

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MedicalServices

import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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

import com.eutech.pawprints.appointments.presentation.appointment.PetAppointmentCard
import com.eutech.pawprints.pets.PetEvents
import com.eutech.pawprints.pets.PetState
import com.eutech.pawprints.pets.dialogs.AddPetInfoDialog
import com.eutech.pawprints.shared.data.pets.Pet
import kotlinx.coroutines.launch


@Composable
fun PetInfo(
    modifier: Modifier = Modifier,
    selectedPet : Pet,
    state : PetState,
    events: (PetEvents) -> Unit,
    onGetPetAppointments : () -> Unit,
    onGetRecords : () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 0)
    val scope = rememberCoroutineScope()
    val tabs = listOf("Basic Info","Medical Info","Appointments")
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
            Box(
                modifier =  modifier.fillMaxWidth().weight(1f)
            ) {
                PetCardInfo(pet = selectedPet)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                AddPetInfoDialog(
                    isLoading = state.isAddingInfo
                ){ label ,value -> Unit
                    events(PetEvents.OnAddInfo(selectedPet.id!!,label,value))
                }
            }
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
                            onGetRecords()
                        }
                        if (index == 2) {
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
                1 -> MedicalInfo(pet = selectedPet,
                    doctors = state.doctors,
                    isLoading = false,
                    records = state.medicalRecords,
                    onSaveMedicalRecord = {record ,images->
                        events(PetEvents.OnSavemedicalRecord(petID = selectedPet.id!!,record,images))
                    }
                )
                2 -> PetAppointmentsLayout(state = state)
            }
        }
    }
}





