package com.eutech.pawprints.products.presentation.view_product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.eutech.pawprints.R
import com.eutech.pawprints.products.data.products.Movement
import com.eutech.pawprints.products.data.products.ProductType
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.products.data.products.getColor
import com.eutech.pawprints.products.presentation.add_stocks.AddStocksDialog
import com.eutech.pawprints.shared.presentation.routes.ProductRouter
import com.eutech.pawprints.shared.presentation.utils.ErrorScreen
import com.eutech.pawprints.shared.presentation.utils.displayDate
import com.eutech.pawprints.shared.presentation.utils.toCreatedAt
import com.eutech.pawprints.shared.presentation.utils.toExpireFormat
import com.eutech.pawprints.shared.presentation.utils.toPhp
import com.eutech.pawprints.shared.presentation.utils.toStocks
import com.eutech.pawprints.shared.presentation.utils.toast
import com.eutech.pawprints.ui.custom.LoadingScreen
import com.eutech.pawprints.ui.custom.PawPrintActionButton
import com.eutech.pawprints.ui.custom.PawPrintText
import com.eutech.pawprints.ui.custom.danger
import com.eutech.pawprints.ui.custom.success
import com.eutech.pawprints.ui.theme.PawPrintsTheme


@Composable
fun ViewProductScreen(
    modifier: Modifier = Modifier,
    productID : String,
    state : ViewProductState,
    events: (ViewProductEvents) -> Unit,
    navHostController: NavHostController,
) {
    val context = LocalContext.current
    LaunchedEffect(productID) {
        if (productID.isNotEmpty()){
            events(ViewProductEvents.OnGetProductByID(productID))
        }
    }
    LaunchedEffect(state) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
        if (state.isAdded != null) {
            context.toast(state.isAdded)
        }
    }

    var addStocksDialog by remember { mutableStateOf(false) }
    if (addStocksDialog) {
        AddStocksDialog(
            onDismiss = {addStocksDialog = !addStocksDialog},
            onConfirm = {quantity ,expiry ->
                events(ViewProductEvents.OnAddStocks(productID,quantity,expiry))
            }
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            state.isLoading -> LoadingScreen(title = "Getting Product Info") 
            state.errors != null -> ErrorScreen(
                title = state.errors,
                image = R.drawable.error
            ) {
                Button(onClick = { navHostController.popBackStack() }) {
                    Text(text = "Back")
                }
            }
            state.product == null -> {
                ErrorScreen(
                    title = "Product not found",
                    image = R.drawable.not_found
                ) {
                    Button(onClick = { navHostController.popBackStack() }) {
                        Text(text = "Back")
                    }
                }
            } else -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(8.dp)

                ) {
                    ViewProductTopBar(
                        product = state.product,
                        isAddingStocks = state.isAddingStocks,
                        onBack = {navHostController.popBackStack()},
                        onDelete = {
                            events.invoke(ViewProductEvents.OnDeleteProduct(
                                productID, context = context,navHostController))
                        },
                        onAddStocks = {
                            addStocksDialog = !addStocksDialog
                        },
                        onEdit = {
                            navHostController.navigate(ProductRouter.EditProduct.navigate(state.product.id ?: ""))
                        }
                    )
                    ViewProductBody(
                        product = state.product
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ViewProductBody(
    modifier: Modifier = Modifier,
    product: Products
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Card(
            modifier = modifier.weight(1f),
        ) {
            Column(
                modifier = modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AsyncImage(
                        modifier = modifier
                            .size(280.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        model = product.image,
                        contentDescription = product.name,
                        error = painterResource(id = R.drawable.product),
                        fallback = painterResource(id = R.drawable.product),
                        placeholder = painterResource(id = R.drawable.product),
                    )
                    Column(
                        modifier = modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ){

                        PawPrintText(data = "Stocks", value = product.quantity.toString())
                        PawPrintText(data = "Cost", value = product.cost.toPhp())
                        PawPrintText(data = "Price", value = product.price.toPhp())
                        PawPrintText(data = "Discount", value = "${product.discount ?: 0 } %")
                        PawPrintText(data = "Type", value = product.type.name)
                        PawPrintText(data = "Expiration", value = product.expiration?.toExpireFormat() ?: "No Date")
                        PawPrintText(data = "CreatedAt", value = product.createdAt.toCreatedAt())
                        PawPrintText(data = "UpdatedAt", value = product.createdAt.toCreatedAt())
                    }

                }

                Spacer(modifier = modifier.height(4.dp))
                Text(text = product.description ?: "")
                Spacer(modifier = modifier.height(4.dp))
                Text(text = "Features", style = MaterialTheme.typography.titleMedium)
                Text(text = product.features ?: "")
                Spacer(modifier = modifier.height(4.dp))
                Text(text = "Contents", style = MaterialTheme.typography.titleMedium)
                Text(text = product.contents ?: "")
            }
        }

        if (product.type == ProductType.GOODS) {
            LazyColumn(
                modifier = modifier.fillMaxSize() .weight(.6f).padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                item {
                    Text(text = "Stock Management", style = MaterialTheme.typography.titleMedium)
                }
                val sortedStocks = product.stocks.sortedByDescending { it.date }

                items(sortedStocks) {
                    Card(
                        modifier = modifier.fillMaxWidth()
                    ){
                        ListItem(
                            text = {
                                Column {
                                    Text("${it.movement.name}", style = MaterialTheme.typography.titleSmall.copy(
                                        color = Color.Gray
                                    ))
                                    Text(it.message, style = MaterialTheme.typography.titleSmall)
                                }

                            },
                            secondaryText = {
                                Text(it.date.displayDate(), style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.Gray
                                ))
                            },
                            trailing = {
                                val text = if (it.movement == Movement.IN) "+${it.quantity}" else "-${it.quantity}"
                                Text(text, color = it.movement.getColor(), style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ))
                            }
                        )
                    }
                }
            }

        }

    }
}

@Composable
fun ViewProductTopBar(
    modifier: Modifier = Modifier,
    product: Products,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onAddStocks: () -> Unit,
    onDelete: () -> Unit,
    isAddingStocks: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back icon")
            }
            Text(text = "${product.name}", style = MaterialTheme.typography.titleLarge)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DeleteConfirmationDialog(product = product, onConfirm = onDelete)
            PawPrintActionButton(
                icon = Icons.Default.Edit,
                title = "Edit",
                onClick = onEdit,
                colors = ButtonDefaults.success()
            )
            PawPrintActionButton(
                icon = Icons.Default.Add,
                title = "Add Stocks",
                onClick =onAddStocks,
            )
        }
    }
}


@Composable
fun DeleteConfirmationDialog(
    product: Products,
    onConfirm : () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete ${product.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onConfirm()
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Dismiss")
                }
            }
        )
    }

    PawPrintActionButton(
        icon = Icons.Default.Delete,
        title = "Delete",
        onClick = {showDialog = true},
        colors = ButtonDefaults.danger()
    )

}
@Preview
@Composable
private fun ViewProductScreenPreview() {
    PawPrintsTheme {
        ViewProductScreen(
            productID = "Hello",
            navHostController = rememberNavController(),
            state = ViewProductState(),
            events = {}
        )
    }
}