package com.eutech.pawprints.doctors.presentation.create_doctor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorLong
import androidx.navigation.NavHostController
import com.eutech.pawprints.products.presentation.create_product.CreateProductEvents
import com.eutech.pawprints.products.presentation.create_product.ImagePicker
import com.eutech.pawprints.shared.presentation.utils.toast
import com.eutech.pawprints.ui.custom.PrimaryButton
import com.eutech.pawprints.ui.custom.PrimaryTextField
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.color.ColorDialog
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.models.ColorSelection
import com.maxkeppeler.sheets.color.models.ColorSelectionMode
import com.maxkeppeler.sheets.color.models.MultipleColors
import com.maxkeppeler.sheets.color.models.SingleColor
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDoctorScreen(
    modifier: Modifier = Modifier,
    state: CreateDoctorState,
    events: (CreateDoctorEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            events.invoke(CreateDoctorEvents.OnProfileChanged(it))
        }
    }
    LaunchedEffect(state) {
        if (!state.errors.isNullOrEmpty()) {
            context.toast(state.errors)
        }
        if (!state.created.isNullOrEmpty()) {
            context.toast(state.created)
            delay(1000L)
            navHostController.popBackStack()
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ){
        TopAppBar(
            title = { Text(text = "Create Doctor") },
            navigationIcon = { IconButton(onClick = { navHostController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }},
        )

        Column(
            modifier = modifier
                .weight(.5f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = modifier.padding(8.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ImagePicker(
                    image = state.profile,
                    onChangeImage = {  imagePickerLauncher.launch("image/*")}
                )
                Column {
                    Text(text = "Tag", style = MaterialTheme.typography.titleMedium)
                    PawPrintsColorPicker(color = state.tag) {
                        events(CreateDoctorEvents.OnColorSelected(it))
                    }
                }
            }

            PrimaryTextField(textField = state.name, label = "Doctor's Fullname") {
                events.invoke(CreateDoctorEvents.OnNameChanged(it))
            }
            PrimaryTextField(
                textField = state.phone,
                label = "Doctor's Phone",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone
                )
            ) {
                events.invoke(CreateDoctorEvents.OnPhoneChanged(it))
            }

            PrimaryTextField(textField = state.email, label = "Doctor's Email") {
                events.invoke(CreateDoctorEvents.OnEmailChanged(it))
            }

            PrimaryButton(label = "Save" , isLoading = state.isLoading) {
                events.invoke(CreateDoctorEvents.OnCreateDoctor)
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PawPrintsColorPicker(
    modifier: Modifier = Modifier,
    color: Int,
    colorSelected: (Int) -> Unit,
) {
    var colorDialog by rememberSaveable { mutableStateOf(false) }
    val templateColors = MultipleColors.ColorsInt(
        Color.Red.copy(alpha = 0.1f).toArgb(),
        Color.Red.copy(alpha = 0.3f).toArgb(),
        Color.Red.copy(alpha = 0.5f).toArgb(),
        Color.Red.toArgb(),
        Color.Green.toArgb(),
        Color.Yellow.toArgb(),
    )

    if (colorDialog) {
        ColorDialog(
            state = rememberUseCaseState(visible = colorDialog, onCloseRequest = { colorDialog = false }),
            selection = ColorSelection(
                selectedColor = SingleColor(color),
                onSelectColor = { colorSelected(it) },
            ),
            config = ColorConfig(
                templateColors = templateColors,
                defaultDisplayMode = ColorSelectionMode.CUSTOM,
                allowCustomColorAlphaValues = false
            ),
        )
    }

    Button(
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(color),
            contentColor = Color.White
        ),

        onClick = {
            colorDialog = true
        }
    ) {
        Text(text = "Select Color", modifier = modifier.padding(
            vertical = 8.dp,
            horizontal = 12.dp
        ))
    }
}
