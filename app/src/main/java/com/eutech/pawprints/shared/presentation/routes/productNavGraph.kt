package com.eutech.pawprints.shared.presentation.routes

import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.eutech.pawprints.products.presentation.create_product.CreateProductScreen
import com.eutech.pawprints.products.presentation.create_product.CreateProductViewModel
import com.eutech.pawprints.products.presentation.product.ProductScreen
import com.eutech.pawprints.products.presentation.product.ProductViewModel
import com.eutech.pawprints.products.presentation.view_product.ViewProductScreen
import com.eutech.pawprints.products.presentation.view_product.ViewProductViewModel


//implement to the navgraph
data object ViewProduct :ProductRouter("view-product/{id}") {
    fun navigate(id : String) : String {
        return "view-product/${id}"
    }
}
fun NavGraphBuilder.productNavGraph(navHostController: NavHostController) {
    navigation(startDestination = ProductRouter.Products.route, route = ProductRouter.Main.route) {
        composable(route = ProductRouter.Products.route) {
            val viewModel = hiltViewModel<ProductViewModel>()
            ProductScreen(
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }

        composable(route = ProductRouter.CreateProduct.route) {
            val viewModel = hiltViewModel<CreateProductViewModel>()
            CreateProductScreen(state = viewModel.state, events = viewModel::events, navHostController = navHostController)
        }

        composable(
            route = ViewProduct.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val viewModel = hiltViewModel<ViewProductViewModel>()
            val productId = backStackEntry.arguments?.getString("id") ?: return@composable
            ViewProductScreen(
                productID = productId,
                state = viewModel.state,
                events = viewModel::events,
                navHostController = navHostController
            )
        }
    }

}