package com.eutech.pawprints.shared.presentation.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
import com.eutech.pawprints.messages.MessageScreen
import com.eutech.pawprints.messages.MessageViewModel
import com.eutech.pawprints.pets.PetScreen
import com.eutech.pawprints.pets.PetViewModel
import com.eutech.pawprints.schedule.presentation.ScheduleScreen
import com.eutech.pawprints.schedule.presentation.ScheduleViewModel
import com.eutech.pawprints.shared.data.messages.Message
import com.eutech.pawprints.users.UsersScreen
import com.eutech.pawprints.users.UsersViewModel
import com.google.firebase.auth.FirebaseAuth


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph(
    mainNav: NavHostController ,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = MainRouter.Home.route
    ) {
        composable(MainRouter.Home.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(state = viewModel.state, events = viewModel::events, navHostController = navHostController)
        }
        composable(MainRouter.Messages.route) {
            val viewModel = hiltViewModel<MessageViewModel>()
            MessageScreen(state = viewModel.state, events = viewModel::events, navHostController = navHostController)
        }
        composable(MainRouter.Pets.route) {
            val viewModel = hiltViewModel<PetViewModel>()
            PetScreen(state = viewModel.state, events = viewModel::events, navController = navHostController)
        }
        composable(MainRouter.Users.route) {
            val viewModel = hiltViewModel<UsersViewModel>()
            UsersScreen(state = viewModel.state, events = viewModel::events, navController = navHostController)
        }

        composable(MainRouter.ScheduleRoute.route) {
            val viewModel = hiltViewModel<ScheduleViewModel>()
            ScheduleScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
        composable(MainRouter.CreateAppointment.route) {
            val viewModel = hiltViewModel<CreateAppointmentViewModel>()
            CreateAppointmentScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
        productNavGraph(navHostController)
        composable(MainRouter.Doctors.route) {
            val viewModel = hiltViewModel<DoctorViewModel>()
            DoctorsScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
        composable(MainRouter.CreateDoctor.route) {
            val viewModel = hiltViewModel<CreateDoctorViewModel>()
            CreateDoctorScreen(
                state = viewModel.state,
                events =viewModel::events ,
                navHostController = navHostController
            )
        }
        composable(
            MainRouter.ViewDoctor.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {backStackEntry ->
            val viewModel = hiltViewModel<ViewDoctorViewModel>()
            val id = backStackEntry.arguments?.getString("id")
            ViewDoctorScreen(
                id = id ?: "",
                state = viewModel.state,
                events =viewModel::events ,
                navHostController = navHostController
            )

        }

        composable(MainRouter.Transactions.route) {
            Text(text = "Transactions")
        }
        composable(MainRouter.Profile.route) {
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(state = viewModel.state, events = viewModel::events, navHostController = navHostController, onLogout = {
                FirebaseAuth.getInstance().signOut()
                mainNav.navigate(AuthRouter.Auth.route)
            })

        }
    }
}