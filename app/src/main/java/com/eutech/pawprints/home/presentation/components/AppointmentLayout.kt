package com.eutech.pawprints.home.presentation.components

import android.icu.text.CaseMap.Title
import android.view.SurfaceControl.Transaction
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eutech.pawprints.appointments.data.appointment.AppointmentStatus
import com.eutech.pawprints.appointments.data.appointment.AppointmentWithAttendeesAndPets
import com.eutech.pawprints.appointments.data.appointment.Appointments
import com.eutech.pawprints.appointments.data.appointment.displayTime
import com.eutech.pawprints.appointments.data.appointment.getColor
import com.eutech.pawprints.appointments.presentation.appointment.AppointmentEvent
import com.eutech.pawprints.appointments.presentation.appointment.DisplayDate
import com.eutech.pawprints.home.presentation.HomeEvents
import com.eutech.pawprints.home.presentation.HomeState
import com.eutech.pawprints.shared.data.transactions.TransactionStatus
import com.eutech.pawprints.shared.data.transactions.TransactionWithUser
import com.eutech.pawprints.shared.presentation.utils.toast
import com.eutech.pawprints.ui.custom.GroupAvatars
import com.eutech.pawprints.ui.custom.Header
import com.eutech.pawprints.ui.custom.Label
import com.eutech.pawprints.ui.custom.SubHeader


@Composable
fun AppointmentLayout(
    modifier: Modifier = Modifier,
    isLoading : Boolean ,
    orders : List<TransactionWithUser>,
    appointments: List<AppointmentWithAttendeesAndPets>,
    onFilterChange: () -> Unit,
    state : HomeState,
    events: (HomeEvents) -> Unit,
    onClickOrder : () -> Unit,
    onNavigateToPos : () -> Unit,
    onNavigateToAppointments : () -> Unit,
 ) {
    val context  = LocalContext.current
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            OrdersCard(
                modifier = modifier.fillMaxWidth().clickable {
                    onClickOrder()
                },
                ongoing = orders.filter { it.transaction.status != TransactionStatus.PENDING }.size,
                pendingOrders = orders.filter { it.transaction.status == TransactionStatus.PENDING }.size,
            )
        }
        item {
            DashboardInfoCard(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigateToPos()
                    },
                icon = Icons.Filled.PointOfSale,
                label = "In Store",
                data = "Point of Sale",
                iconBackGround = Color(0xFF2E7D32),
            ) {

            }
        }
        item {
            AppointmentInfoCard(
                modifier = modifier.fillMaxWidth().clickable {
                    onNavigateToAppointments()
                },
                pending = appointments.filter { it.appointments.status == AppointmentStatus.PENDING }.size,
                upcoming = appointments.filter { it.appointments.status == AppointmentStatus.CONFIRMED }.size,
            )
        }
        item {
            Text(
                text = if (isLoading ) "Loading..." else "Appointments",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = modifier.padding(
                    bottom = 8.dp
                )
            )
        }
        val pendingAppointments = appointments.filter { it.appointments.status == AppointmentStatus.PENDING }
        items(pendingAppointments) {
            AppointmentCard(data = it, isLoading = state.updatingStatus, onUpdateStatus = { s, a ->
                events.invoke(HomeEvents.OnUpdateAppointment(s,a))
                context.toast("test")
            })
        }
    }
}


@Composable
fun OrdersCard(
    modifier: Modifier = Modifier,
    pendingOrders : Int,
    ongoing : Int
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            4.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = Color(0xFF2E7D32),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Orders",
                    tint = Color.White
                )
            }
            Spacer(
                modifier = Modifier.width(12.dp)
            )
            Column(
                modifier = modifier.weight(1f)
            ) {
                Text(
                    "Pending",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "${pendingOrders}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Column(
                modifier = modifier.weight(1f)
            ) {
                Text(
                    "Ongoing",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "${ongoing}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}




@Composable
fun AppointmentInfoCard(
    modifier: Modifier = Modifier,
    pending : Int,
    upcoming : Int
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            4.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = Color(0xFF2E7D32),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = "Orders",
                    tint = Color.White
                )
            }
            Spacer(
                modifier = Modifier.width(12.dp)
            )
            Column(
                modifier = modifier.weight(1f)
            ) {
                Text(
                    "Pending",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "${pending}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Column(
                modifier = modifier.weight(1f)
            ) {
                Text(
                    "Upcoming",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "${upcoming}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
