package com.eutech.pawprints.shared.presentation.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eutech.pawprints.R
import com.eutech.pawprints.appointments.presentation.appointment.AppointmentScreen
import com.eutech.pawprints.appointments.presentation.appointment.AppointmentViewModel
import com.eutech.pawprints.appointments.presentation.create_appointment.CreateAppointmentScreen
import com.eutech.pawprints.appointments.presentation.create_appointment.CreateAppointmentViewModel
import com.eutech.pawprints.auth.presentation.profile.ProfileScreen
import com.eutech.pawprints.auth.presentation.profile.ProfileViewModel
import com.eutech.pawprints.doctors.presentation.create_doctor.CreateDoctorScreen
import com.eutech.pawprints.doctors.presentation.create_doctor.CreateDoctorViewModel
import com.eutech.pawprints.doctors.presentation.doctor.DoctorViewModel
import com.eutech.pawprints.doctors.presentation.doctor.DoctorsScreen
import com.eutech.pawprints.doctors.presentation.view_doctor.ViewDoctorScreen
import com.eutech.pawprints.doctors.presentation.view_doctor.ViewDoctorViewModel
import com.eutech.pawprints.home.presentation.HomeScreen
import com.eutech.pawprints.home.presentation.HomeViewModel
import com.eutech.pawprints.products.presentation.view_product.ViewProductViewModel
import com.eutech.pawprints.schedule.presentation.ScheduleScreen
import com.eutech.pawprints.schedule.presentation.ScheduleViewModel
import com.eutech.pawprints.shared.presentation.routes.AuthRouter
import com.eutech.pawprints.shared.presentation.routes.MainNavGraph
import com.eutech.pawprints.shared.presentation.routes.MainRouter
import com.eutech.pawprints.shared.presentation.routes.ProductRouter
import com.eutech.pawprints.shared.presentation.routes.productNavGraph
import com.eutech.pawprints.ui.custom.EmailAvatar
import com.google.firebase.auth.FirebaseAuth


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: MainState,
    events: (MainEvents) -> Unit,
    mainNavController : NavHostController,
    navHostController: NavHostController = rememberNavController()
) {
    val items = NavigationRailItems.entries.toList()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
    LaunchedEffect(state.unseenMessages) {
        items[1].badgeCount = state.unseenMessages.size
    }

    Row(
        modifier = modifier.fillMaxSize()
    ) {
        if (items.any { it.route == currentRoute?.route } || currentRoute?.route == ProductRouter.Products.route) {
            MainNavigationRail(
                navController = navHostController,
                navBackStackEntry = navBackStackEntry,
                items = items
            )
        }
        Scaffold {
            Box(
                modifier = modifier.fillMaxSize().padding(it)
            ) {
                MainNavGraph(
                    mainNav = mainNavController,
                    navHostController = navHostController
                )
            }
        }

    }
}

@Composable
private fun MainNavigationRail(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry ?,
    items : List<NavigationRailItems>,
) {
    val currentRoute = navBackStackEntry?.destination
    NavigationRail(
        modifier = modifier.padding(start = 8.dp, end = 8.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items.forEachIndexed { index, it ->
                val isSelected = it.route == currentRoute?.route
                val isProduct = if (index == 5) currentRoute?.route in listOf(
                    ProductRouter.Products.route,
                    ProductRouter.ViewProduct.route,
                    ProductRouter.EditProduct.route
                ) else false
                NavigationRailItem(
                    selected = isSelected || isProduct,
                    onClick = {
                        navController.navigate(it.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        BadgedBox(badge = {
                            if (it.badgeCount != null) {
                                Badge {
                                    Text(text = it.badgeCount.toString())
                                }
                            } else if (it.hasNews) {
                                Badge()
                            }
                        }) {
                            if (isSelected) {
                                Icon(painter = painterResource(it.selectedIcon), contentDescription = it.route)
                            } else {
                                Icon(painter = painterResource(it.unselectedIcon), contentDescription = it.route)
                            }
                        }
                        Icon(painter = painterResource(id = if (isSelected || isProduct) it.selectedIcon else it.unselectedIcon), contentDescription = "${it.label} icon")
                           },
                    alwaysShowLabel = isSelected || isProduct,
                    label = { Text(
                        text = it.label,
                        style = MaterialTheme.typography.bodySmall)
                    }
                )
            }
        }
    }
}
