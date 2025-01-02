package com.eutech.pawprints.products.presentation.create_product

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.eutech.pawprints.R
import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.data.products.ProductType
import com.eutech.pawprints.shared.presentation.utils.TextFieldData
import com.eutech.pawprints.ui.custom.PrimaryButton
import com.eutech.pawprints.ui.custom.PrimaryTextField
import kotlinx.coroutines.delay

@Composable
fun CreateProductScreen(
    modifier: Modifier = Modifier,
    state : CreateProductState,
    events: (CreateProductEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
           events(CreateProductEvents.OnImageChanged(it))
        }
    }
    LaunchedEffect(state) {
        if (state.errors != null) {
            Toast.makeText(context,state.errors,Toast.LENGTH_SHORT).show()
        }
        if (state.created!= null) {
            Toast.makeText(context,state.created,Toast.LENGTH_SHORT).show()
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
                ImagePicker(
                    image = state.image,
                    onChangeImage = {  imagePickerLauncher.launch("image/*") }
                )
            }
            ProductInfo(state = state,events  = events)
            PrimaryButton(
                label = "Create",
                isLoading = state.isLoading,
                enabled = !state.formHasError || state.isLoading,
                onClick = {
                    events(CreateProductEvents.SubmitProduct(context,navHostController))
                }
            )
        }
    }
}

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier,
    default :String ? = null,
    image: Uri?,
    onChangeImage : () -> Unit
) {
    Column (
        modifier = modifier
            .width(250.dp)
            .clip(RoundedCornerShape(8.dp))
    ){
        Image(
            painter = if (image != null)
                rememberAsyncImagePainter(image)
            else
                painterResource(id = R.drawable.product),
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

@Composable
fun ProductInfo(
    modifier: Modifier = Modifier,
    state: CreateProductState,
    events: (CreateProductEvents) -> Unit
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
                onValueChange = {events(CreateProductEvents.OnTextFieldChange(it,FormField.NAME))}
            )

            //make this downdown with add category button in the last of list
            CategoryDropdown(
                modifier = modifier.weight(.4f),
                categories =state.categoryList,
                selectedCategory = state.selectedCategory,
                onCategorySelected = {events(CreateProductEvents.OnSelectCategory(it))},
                onAddCategory = {
                    events.invoke(CreateProductEvents.OnCreateCategory(it))
                },
                onDeleteCategory = {
                    events(CreateProductEvents.DeleteCategory(it))
                }
            )
            ProductTypeDropdown(
                modifier = modifier.weight(.4f),
                types = ProductType.entries.toList(),
                selectedType = state.selectedProductType,
                onTypeSelected = {events(CreateProductEvents.OnProductTypeChange(it))}
            )
        }
        Row {
            PrimaryTextField(
                modifier = modifier.weight(1f),
                textField = state.quantity,
                label = "Quantity",
                onValueChange = {events(CreateProductEvents.OnTextFieldChange(it,FormField.QUANTITY))}
            )
            PrimaryTextField(
                modifier = modifier.weight(1f),
                textField = state.cost,
                label = "Cost",
                onValueChange = {events(CreateProductEvents.OnTextFieldChange(it,FormField.COST))}
            )
            PrimaryTextField(
                modifier = modifier.weight(1f),
                textField = state.price,
                label = "Price",
                onValueChange = {events(CreateProductEvents.OnTextFieldChange(it,FormField.PRICE))}
            )
        }

        PrimaryTextField(
            textField = state.desc,
            label = "Description",
            isOptional = true,
            minLines = 3,
            onValueChange = {events(CreateProductEvents.OnTextFieldChange(it,FormField.DESC))}
        )
        PrimaryTextField(
            textField = state.features,
            label = "Features",
            isOptional = true,
            minLines = 3,
            onValueChange = {events(CreateProductEvents.OnTextFieldChange(it,FormField.FEATURES))}
        )
        PrimaryTextField(
            textField = state.contents,
            label = "Contents",
            minLines = 3,
            onValueChange = {events(CreateProductEvents.OnTextFieldChange(it,FormField.CONTENTS))}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductTypeDropdown(
    modifier: Modifier,
    types: List<ProductType>,
    selectedType: ProductType,
    onTypeSelected: (ProductType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        expanded = expanded,
        onExpandedChange =  { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            maxLines = 1,
            shape = RoundedCornerShape(4.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            value = selectedType.name,
            onValueChange = { onTypeSelected(selectedType)},
            label = { Text("Select Product Type") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            isError = selectedType.name.isEmpty(),
            supportingText = {
                Text(
                    text = "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Start
                )
            },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize()
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category ?) -> Unit,
    onAddCategory: (String) -> Unit,
    onDeleteCategory : (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        expanded = expanded,
        onExpandedChange =  { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            maxLines = 1,
            shape = RoundedCornerShape(4.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            value = selectedCategory?.name ?: "No category Selected",
            onValueChange = { onCategorySelected(selectedCategory)},
            label = { Text("Select category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            isError = selectedCategory == null,
            supportingText = {
                Text(
                    text = if( selectedCategory == null ) "No category selected" else "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Start
                )
            },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize()
        ) {
            categories.forEach { section ->
                DropdownMenuItem(
                    text = { Text(section.name?: "no category") },
                    trailingIcon = {
                        IconButton(onClick = {onDeleteCategory(section.id ?: "")}) {
                            Icon(
                                imageVector =Icons.Default.Close,
                                contentDescription = "Delete"
                            )
                        }
                    },
                    onClick = {
                        onCategorySelected(section)
                        expanded = false
                    }
                )
            }
            val c = categories.map { it.name?.lowercase() ?: "" }
            CreateCategoryDialog(categories = c) {
                onAddCategory(it)
            }
        }
    }
}

@Composable
fun CreateCategoryDialog(
    categories: List<String>,
    onCreate : (String) -> Unit
) {
    var category by remember { mutableStateOf(TextFieldData()) }
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = !showDialog },
            title = {
                Text(text = "Create new Category")
            },
            text = {
                Column {
                    PrimaryTextField(
                        textField = category,
                        label = "Category",
                        onValueChange = {
                            val hasError = it.isEmpty() || categories.contains(it.lowercase())
                            val errorMessage = if (hasError) "Invalid category" else null
                            val newName = category.copy(
                                value = it,
                                hasError = hasError,
                                errorMessage = errorMessage
                            )
                            category = newName
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = !category.hasError,
                    onClick = {
                        onCreate(category.value)
                        showDialog = !showDialog
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {showDialog = !showDialog}) {
                    Text("Cancel")
                }
            }
        )
    }

    DropdownMenuItem(
        colors = MenuDefaults.itemColors(
            textColor = MaterialTheme.colorScheme.primary,
            leadingIconColor = MaterialTheme.colorScheme.primary
        ),
        leadingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add")},
        text = { Text(text = "Add Category") },
        onClick = {
            showDialog = !showDialog
        }
    )

}

