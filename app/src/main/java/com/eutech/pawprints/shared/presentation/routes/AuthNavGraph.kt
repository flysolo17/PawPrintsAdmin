package com.eutech.pawprints.shared.presentation.routes

import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.eutech.pawprints.auth.presentation.login.LoginScreen
import com.eutech.pawprints.auth.presentation.login.LoginViewModel


fun NavGraphBuilder.authNavGraph(navHostController: NavHostController) {
    navigation(startDestination = AuthRouter.Login.route,route = AuthRouter.Auth.route) {
        composable(route = AuthRouter.Login.route) {
            val viewmodel = hiltViewModel<LoginViewModel>()
            val state = viewmodel.state
            val events = viewmodel::events
            LoginScreen(navHostController = navHostController, state = state, events = events)
        }
        composable(route = AuthRouter.Register.route) {
            Text(text = "register")
        }
        composable(route = AuthRouter.ForgotPassword.route) {
            Text(text = "Forgot Password")
        }
    }
}
