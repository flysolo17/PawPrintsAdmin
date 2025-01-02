package com.eutech.pawprints.pets.view_pets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eutech.pawprints.pets.PetEvents
import com.eutech.pawprints.pets.components.MedicalInfo
import com.eutech.pawprints.pets.components.PetAppointmentsLayout
import com.eutech.pawprints.pets.components.PetDetails
import com.eutech.pawprints.pets.dialogs.AddPetInfoDialog
import com.eutech.pawprints.shared.presentation.utils.ErrorScreen
import com.eutech.pawprints.shared.presentation.utils.toast
import com.eutech.pawprints.ui.custom.Avatar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ViewPetScreen(
    modifier: Modifier = Modifier,
    id : String,
    state: ViewPetState,
    events: (ViewPetEvents) -> Unit,
    navHostController: NavHostController
) {

    val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 0)
    val scope = rememberCoroutineScope()
    val tabs = listOf("Basic Info","Medical Info","Appointments")
    val context  = LocalContext.current
    LaunchedEffect(id) {
        if (id.isNotEmpty()) {
            events(ViewPetEvents.OnGetPetInfo(id))
        }
    }
    LaunchedEffect(state.isPetDeleted) {
        state.isPetDeleted?.let {
            context.toast(it)
            delay(1000)
            navHostController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (state.petWithOwner != null) {
                        ListItem(
                            icon = {
                                Avatar(
                                    image = "${state.petWithOwner.pet?.image}",
                                    size = 42.dp
                                ) { }
                            },
                            text = {
                                Text("${state.petWithOwner.pet?.name}")
                            },
                            secondaryText = {
                                Text("${state.petWithOwner?.pet?.species}", style = MaterialTheme.typography.labelSmall)
                            }
                        )
                    } else {
                        Text("View Pet")
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {navHostController.popBackStack()}
                    ) { Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    ) }
                },
                actions = {
                    DeleteConfirmationDialog(
                        title = "Delete Pet",
                        body = "Are you sure you want to delete ${state.petWithOwner?.pet?.name}? This action cannot be undone.",
                        onConfirm = {
                            events(ViewPetEvents.OnDeletePet(id))
                        }
                    )
                    if (state.petWithOwner != null) {
                        AddPetInfoDialog(
                            isLoading = state.isAddingInfo
                        ){ label ,value -> Unit
                            events(ViewPetEvents.OnAddInfo(state.petWithOwner.pet?.id!!,label,value))
                        }
                    }
                }
            )
        }
    ) {
        Column(
            modifier = modifier.fillMaxSize().padding(it)
        ) {
            if (state.isLoading) {
                LinearProgressIndicator(modifier = modifier.fillMaxWidth())
            }
            if (state.errors!= null) {
                ErrorScreen(title = state.errors) {
                    Button(onClick = {navHostController.popBackStack()}) { Text("Back") }
                }
            }
            if (state.petWithOwner!= null) {
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
                                    events(ViewPetEvents.OngetMedicalRecord(petID = id))
                                }
                                if (index == 2) {
                                    events(ViewPetEvents.OnGetPetAppointments(id))
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
                        0 -> PetDetails(pet = state.petWithOwner.pet!!, owner = state.petWithOwner.owner)
                        1 -> MedicalInfo(pet = state.petWithOwner.pet!!,
                            doctors = state.doctors,
                            isLoading = false,
                            records = state.records,
                            onSaveMedicalRecord = {record ,images->
                                events(ViewPetEvents.OnSavemedicalRecord(petID = id,record,images))
                            }
                        )
                        2 -> PetAppointmentsLayout(
                            isLoading =  state.isGettingPetSchedule,
                            appointments = state.appointments,
                            error = state.petAppointmentError
                        )
                    }
                }

            }

        }
    }
}