package com.eutech.pawprints.products.presentation.edit_product

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.eutech.pawprints.R
import com.eutech.pawprints.products.data.products.ProductType
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.products.presentation.create_product.CategoryDropdown
import com.eutech.pawprints.products.presentation.create_product.FormField
import com.eutech.pawprints.products.presentation.create_product.ImagePicker
import com.eutech.pawprints.products.presentation.create_product.ProductTypeDropdown
import com.eutech.pawprints.shared.presentation.components.DatePickerFilled
import com.eutech.pawprints.shared.presentation.components.PawPrintDatePicker
import com.eutech.pawprints.shared.presentation.utils.toast
import com.eutech.pawprints.ui.custom.PrimaryButton
import com.eutech.pawprints.ui.custom.PrimaryTextField
import kotlinx.coroutines.delay


@Composable
fun EditProductScreen(
    modifier: Modifier = Modifier,
    productID : String ?,
    state: EditProductState,
    events: (EditProductEvents) -> Unit,
    navHostController: NavHostController
) {

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            events(EditProductEvents.OnImageChanged(it))
        }
    }
    LaunchedEffect(productID) {
        if (productID != null) {
            events(EditProductEvents.OnGetProductByID(productID))
        }

    }
    LaunchedEffect(state) {
        if (state.errors != null) {
            Toast.makeText(context,state.errors, Toast.LENGTH_SHORT).show()
        }
        if (state.isSaved != null) {
            context.toast(state.isSaved)
            delay(1000)
            navHostController.popBackStack()
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {navHostController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
            }
            Text(text = "Create Product", style = MaterialTheme.typography.titleLarge)

        }
        //forms
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = modifier.padding(8.dp)) {
                if (state.image == null) {
                    DisplayImage(image = state.products?.image, onChangeImage = {
                        imagePickerLauncher.launch("image/*")
                    })
                } else {
                    ImagePicker(
                        image = state.image,
                        onChangeImage = {  imagePickerLauncher.launch("image/*") }
                    )
                }

            }
            EditProductInfo(state = state,events  = events)
            PrimaryButton(
                label = "Save",
                isLoading = state.isLoading,
                enabled = !state.formHasError || state.isLoading,
                onClick = {
                    state.products?.let {
                        events(EditProductEvents.OnSave(it))
                    }

                }
            )
        }
    }
}

@Composable
fun EditProductInfo(
    modifier: Modifier = Modifier,
    state: EditProductState,
    events: (EditProductEvents) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {

        Text(text = "Please fill up the all forms", style = MaterialTheme.typography.titleLarge)
        Text(text = "For better listings", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
        Spacer(modifier = modifier.height(8.dp))
        Row(
        ) {
            PrimaryTextField(
                modifier = modifier.weight(1f),
                textField = state.name,
                label = "Name",
                onValueChange = {events(EditProductEvents.OnTextFieldChange(it, FormField.NAME))}
            )

            CategoryDropdown(
                modifier = modifier.weight(.4f),
                categories =state.categoryList,
                selectedCategory = state.selectedCategory,
                onCategorySelected = {events(EditProductEvents.OnSelectCategory(it))},
                onAddCategory = {

                })
            ProductTypeDropdown(
                modifier = modifier.weight(.4f),
                types = ProductType.entries.toList(),
                selectedType = state.selectedProductType,
                onTypeSelected = {events(EditProductEvents.OnProductTypeChange(it))}
            )
        }
        Row {
            PrimaryTextField(
                modifier = modifier.weight(1f),
                textField = state.quantity,
                label = "Quantity",
                onValueChange = {events(EditProductEvents.OnTextFieldChange(it, FormField.QUANTITY))}
            )
            PrimaryTextField(
                modifier = modifier.weight(1f),
                textField = state.cost,
                label = "Cost",
                onValueChange = {events(EditProductEvents.OnTextFieldChange(it, FormField.COST))}
            )
            PrimaryTextField(
                modifier = modifier.weight(1f),
                textField = state.price,
                label = "Price",
                onValueChange = {events(EditProductEvents.OnTextFieldChange(it, FormField.PRICE))}
            )
            DatePickerFilled(
                modifier = modifier.weight(1f),
                label = "Expiration",
                value = state.expiration,
                onChange = {
                    events(EditProductEvents.OnExpirationSave(it))
                }
            )
        }

        PrimaryTextField(
            textField = state.desc,
            label = "Description",
            isOptional = true,
            minLines = 3,
            onValueChange = {events(EditProductEvents.OnTextFieldChange(it, FormField.DESC))}
        )
        PrimaryTextField(
            textField = state.features,
            label = "Features",
            isOptional = true,
            minLines = 3,
            onValueChange = {events(EditProductEvents.OnTextFieldChange(it, FormField.FEATURES))}
        )
        PrimaryTextField(
            textField = state.contents,
            label = "Contents",
            minLines = 3,
            onValueChange = {events(EditProductEvents.OnTextFieldChange(it, FormField.CONTENTS))}
        )
    }
}

@Composable
fun DisplayImage(
    modifier: Modifier = Modifier,
    image: String ?,
    onChangeImage : () -> Unit
) {
    Column (
        modifier = modifier
            .width(250.dp)
            .clip(RoundedCornerShape(8.dp))
    ){
        AsyncImage(
            model = image,
            placeholder = painterResource(id = R.drawable.product),
            error = painterResource(id = R.drawable.product),
            contentDescription = "Product Image",
            modifier = modifier
                .width(250.dp)
                .height(200.dp)
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )
        Button(
            onClick = onChangeImage,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Image")
                Spacer(modifier = modifier.width(8.dp))
                Text(text = "${if (image == null) "Add" else "Change"} Image")
            }
        }
    }
}