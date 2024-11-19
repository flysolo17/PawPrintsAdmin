package com.eutech.pawprints.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eutech.pawprints.R
import com.eutech.pawprints.shared.presentation.routes.MainRouter
import com.eutech.pawprints.ui.custom.PrimaryButton
import com.eutech.pawprints.ui.custom.PrimaryOutlineButton
import com.eutech.pawprints.ui.custom.PrimaryTextField
import com.eutech.pawprints.ui.theme.PawPrintsTheme
import kotlinx.coroutines.time.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState,
    events: (LoginEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(
        state
    ) {
        if (state.admin != null) {
            Toast.makeText(context,"Successfully Logged in",Toast.LENGTH_SHORT).show()
            navHostController.navigate(MainRouter.Main.route)
        }
        if (state.errors != null) {
            Toast.makeText(context,state.errors,Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold() {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HeroSection(
                modifier = Modifier.weight(1f)
            )
            LoginFormSection(
                state = state,
                events = events,
                modifier = modifier.weight(1f)
            )
        }
    }
}

@Composable
fun LoginFormSection(
    modifier: Modifier = Modifier,
    state: LoginState,
    events: (LoginEvents) -> Unit
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello, Admin",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "Welcome back you've been missed!",
            fontWeight = FontWeight.Medium,
            modifier = modifier.padding(16.dp)
        )
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
        ) {
            PrimaryTextField(
                textField = state.email,
                label = "Email",
                onValueChange = {events.invoke(LoginEvents.OnEmailChanged(it))}
            )
            TextField(
                value = state.password.value,
                maxLines = 1,
                isError = state.password.hasError,
                onValueChange = {events(LoginEvents.OnPasswordChange(it))},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = {
                        events(LoginEvents.OnTogglePassword)
                    }) {
                        Icon(
                            painter = painterResource(id = if (state.isPasswordVisible) R.drawable.ic_eye_off else R.drawable.ic_eye),
                            contentDescription = if (state.isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                label = {
                    Text(text = "Password")
                },
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text(text = state.password.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Start,
                    )
                } ,
                shape = RoundedCornerShape(4.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                singleLine = true,
            )
            TextButton(onClick = { /*TODO*/ }) {
                Text("Forgot Password")
            }
        }

        PrimaryButton(
            label = "Login",
            isLoading = state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            events.invoke(LoginEvents.OnLogin(state.email.value,state.password.value))
        }
//        PrimaryOutlineButton(label = "No account yet ? Sign up here!") {
//
//        }
    }
}

@Composable
fun HeroSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = modifier
                .height(140.dp)
                .width(140.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo"
        )
        Text(
            text = "Paw Prints",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "Aucena Veterinary \n" +
                    "Clinic",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = modifier.padding(16.dp)
        )
    }
}


@Preview(showBackground = true,)
@Composable
private fun LoginScreenPrev() {
    PawPrintsTheme {
        LoginFormSection(state = LoginState()) {
            
        }
    }
}