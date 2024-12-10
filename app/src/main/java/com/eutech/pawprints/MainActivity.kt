package com.eutech.pawprints

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eutech.pawprints.auth.presentation.login.LoginScreen
import com.eutech.pawprints.auth.presentation.login.LoginViewModel
import com.eutech.pawprints.shared.presentation.main.MainScreen
import com.eutech.pawprints.shared.presentation.main.MainViewModel
import com.eutech.pawprints.shared.presentation.routes.AuthRouter
import com.eutech.pawprints.shared.presentation.routes.MainRouter
import com.eutech.pawprints.shared.presentation.routes.authNavGraph
import com.eutech.pawprints.ui.theme.PawPrintsTheme
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PawPrintsTheme {
                Surface {
                    MainNavHost()
                }

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavHost(
    modifier: Modifier = Modifier
) {
    val controller = rememberNavController()
    NavHost(navController = controller, startDestination = AuthRouter.Auth.route) {
        authNavGraph(navHostController = controller)
        composable(MainRouter.Main.route) {
            val viewModel = hiltViewModel<MainViewModel>()
            MainScreen(mainNavController = controller, state = viewModel.state, events = viewModel::events)
        }
    }

}





