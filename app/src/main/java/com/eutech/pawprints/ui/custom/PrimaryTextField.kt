package com.eutech.pawprints.ui.custom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eutech.pawprints.auth.presentation.login.LoginEvents
import com.eutech.pawprints.shared.presentation.utils.TextFieldData
import com.eutech.pawprints.ui.theme.PawPrintsTheme

@Composable
fun PrimaryTextField(
    modifier: Modifier = Modifier,
    textField : TextFieldData,
    label : String,
    minLines : Int = 1,
    isOptional : Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange : (text : String) -> Unit
) {
    TextField(
        value = textField.value,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        label = { Text(text = label) },
        keyboardOptions = keyboardOptions,
        minLines = minLines,

        onValueChange = {onValueChange(it)},
        isError = textField.hasError,
        shape = RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
        supportingText = {
            if (isOptional) {
                Text(text = "(Optional)")
            } else {
                val errorMessage = textField.errorMessage ?: ""
                Text(text = errorMessage)
            }

        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PrimaryTextField(
) {
    PawPrintsTheme {
        PrimaryTextField(textField = TextFieldData(), label = "Email") {

        }
    }
}