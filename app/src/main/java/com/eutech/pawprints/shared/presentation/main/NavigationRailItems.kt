package com.eutech.pawprints.shared.presentation.main

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import com.eutech.pawprints.R
import com.eutech.pawprints.shared.presentation.routes.MainRouter
import com.eutech.pawprints.shared.presentation.routes.ProductRouter

enum class NavigationRailItems(
    val label : String,
    @DrawableRes val selectedIcon : Int,
    @DrawableRes val unselectedIcon : Int,
    val hasNews : Boolean,
    val badgeCount : Int? = null,
    val route : String
){

    HOME(
        label = "Home",
        selectedIcon = R.drawable.home_bold,
        unselectedIcon = R.drawable.home_outline,
        hasNews = false,
        route = MainRouter.Home.route,
    ),

    MESSAGE(
        label = "Messages",
        selectedIcon = R.drawable.baseline_message_24,
        unselectedIcon = R.drawable.outline_message_24,
        hasNews = false,
        route = MainRouter.Messages.route,
    ),
    PETS(
        label = "Pets",
        selectedIcon = R.drawable.baseline_pets_24,
        unselectedIcon = R.drawable.outline_pets_24,
        hasNews = false,
        route = MainRouter.Pets.route,
    ),

    USERS(
        label = "Users",
        selectedIcon = R.drawable.users_bold,
        unselectedIcon = R.drawable.users_light,
        hasNews = false,
        route = MainRouter.Users.route,
    ),

    SCHEDULE(
        label = "Schedule",
        selectedIcon = R.drawable.calendar_bold,
        unselectedIcon = R.drawable.calendar_outline,
        hasNews = false,
        route = MainRouter.ScheduleRoute.route,
    ),

    PRODUCTS(
        label = "Products",
        selectedIcon = R.drawable.bag_bold,
        unselectedIcon = R.drawable.bag_outline,
        hasNews = false,
        route = ProductRouter.Main.route,
    ),
    DOCTORS(
        label = "Doctors",
        selectedIcon = R.drawable.doctor_filled,
        unselectedIcon = R.drawable.doctor,
        hasNews = false,
        route = MainRouter.Doctors.route,
    ),
    TRANSACTIONS(
        label = "Transactions",
        selectedIcon = R.drawable.chart_bold,
        unselectedIcon = R.drawable.chart_outline,
        hasNews = false,
        route = MainRouter.Transactions.route,
    ),
    PROFILE(
        label = "Profile",
        selectedIcon = R.drawable.profile_bold,
        unselectedIcon = R.drawable.profile_outline,
        hasNews = false,
        route = MainRouter.Profile.route,
    ),
}