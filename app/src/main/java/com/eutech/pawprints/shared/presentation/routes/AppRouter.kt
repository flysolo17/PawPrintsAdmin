package com.eutech.pawprints.shared.presentation.routes

import com.eutech.pawprints.products.data.products.Products
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


sealed class AuthRouter(
    val route : String
) {
    data object Auth : AuthRouter("auth")
    data object Login : AuthRouter("login")
    data object Register : AuthRouter("register")
    data object ForgotPassword  : AuthRouter("forgot-password")
    data object ChangePassword : AuthRouter("change-password")
}

sealed class MainRouter(val route : String) {
    data object Main : MainRouter("main")
    data object Home : MainRouter("home")
    data object Pos : MainRouter("pos")
    data object Messages : MainRouter("messages")
    data object Pets : MainRouter("pets")
    data object VIEW_PETS : MainRouter("pets/{id}") {
        fun createRoute(id: String): String = "pets/$id"
    }
    data object Users : MainRouter("users")
    data object ScheduleRoute : MainRouter("schedules")
    data object Appointments : MainRouter("appointments")


    data object CreateAppointment : MainRouter("create-appointment")


    data object Orders : MainRouter("orders")
    data object Doctors : MainRouter("doctors")
    data object CreateDoctor : MainRouter("create_doctor")
    data object ViewDoctor : MainRouter("view-doctor/{id}") {
        fun navigate(id : String) : String {
            return "view-doctor/${id}"
        }
    }

    data object Profile : MainRouter("profile")
    data object Transactions : MainRouter("transactions")
}


sealed class ProductRouter(val route: String) {
    data object Main : ProductRouter("product")
    data object Products : ProductRouter("products")
    data object CreateProduct :ProductRouter("create-product")

    data object EditProduct : MainRouter("edit-product/{id}") {
        fun navigate(id : String) : String {
            return "edit-product/${id}"
        }
    }

    data object ViewProduct :ProductRouter("view-product/{id}") {
        fun navigate(id : String) : String {
            return "view-product/${id}"
        }
    }
    data object UpdateProduct : ProductRouter("update-product")
}

