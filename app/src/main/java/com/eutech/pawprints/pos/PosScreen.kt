package com.eutech.pawprints.pos

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eutech.pawprints.orders.OrderEvents
import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.presentation.product.CategoryTabItem
import com.eutech.pawprints.products.presentation.product.CategoryTabs
import com.eutech.pawprints.products.presentation.product.ProductCard
import com.eutech.pawprints.products.presentation.product.ProductEvents
import com.eutech.pawprints.products.presentation.product.ProductState
import com.eutech.pawprints.shared.data.transactions.Payment
import com.eutech.pawprints.shared.data.transactions.PaymentStatus
import com.eutech.pawprints.shared.data.transactions.PaymentType
import com.eutech.pawprints.shared.data.transactions.Transaction
import com.eutech.pawprints.shared.data.transactions.TransactionItems
import com.eutech.pawprints.shared.data.transactions.TransactionStatus
import com.eutech.pawprints.shared.data.transactions.TransactionType
import com.eutech.pawprints.shared.presentation.components.PaymentDialog
import com.eutech.pawprints.shared.presentation.routes.ProductRouter
import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import com.eutech.pawprints.shared.presentation.utils.toPhp
import com.eutech.pawprints.shared.presentation.utils.toast


@Composable
fun PosScreen(
    modifier: Modifier = Modifier,
    state: PosState,
    events: (PosEvents) -> Unit,
    navHostController: NavHostController,
) {
    val context = LocalContext.current
    LaunchedEffect(
        state.items
    ) {
        events(PosEvents.ComputeTotal)
    }

    LaunchedEffect(state) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
        if (state.isSubmitted != null) {
            context.toast(state.isSubmitted)
        }
    }
    if (state.showPaymentDialog) {
        PaymentDialog(
            amount = state.total,
            amountReceived = state.amountReceived,
            isPaying = state.isSubmitting,
            onChange = {events(PosEvents.OnAmountReceivedChange(it))},
            onConfirm = {
                val transaction : Transaction = Transaction(
                    id = generateRandomNumber(),
                    items = state.items,
                    type = TransactionType.IN_STORE,
                    status = TransactionStatus.COMPLETED,
                    payment = Payment(
                        id = generateRandomNumber(),
                        total = state.total,
                        status = PaymentStatus.PAID,
                        type = PaymentType.CASH
                    )
                )
                events(PosEvents.SaveTransaction(transaction))
            },
            onDismiss = {
                events(PosEvents.Submit)
            }
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ){
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = {navHostController.popBackStack()}
            ) { Icon(Icons.Filled.ArrowBack, contentDescription = "back", tint = MaterialTheme.colorScheme.onSurface) }
            Text("Point of Sale", style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ))
        }
        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = modifier.fillMaxWidth()
            )
        }
        Row(
            modifier = modifier.fillMaxSize()
        ){
            LazyVerticalGrid(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(8.dp),
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(
                    span = { GridItemSpan(4) }
                ) {
                    PosTabs(state = state, events = events)
                }
                val selectedIndex = state.selectedCategoryIndex
                val items =
                    if (state.selectedCategoryIndex == 0) state.products
                    else state.products.filter {it.categoryID == state.categoryList[selectedIndex].id}
                items(items, key = {it.id  ?: generateRandomNumber() }) {
                    ProductCard(
                        product = it,
                        onClick = {
                            events(PosEvents.OnSelectProduct(it))
                        },
                    )
                }
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .weight(0.5f)
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    itemsIndexed(state.items) { index ,item ->
                        PosSelectedItems(
                            index = index,
                            item = item,
                            onIncrease = {events(PosEvents.IncreaseQuantity(index))},
                            onDecreaseQuantity = {events(PosEvents.DecreaseQuantity(index))}
                        )
                    }
                }
                Button(
                    shape = MaterialTheme.shapes.small,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {events(PosEvents.Submit)}
                ) {
                    Text("Pay now (${state.total.toPhp()})", modifier = modifier.padding(8.dp))
                }
            }
        }

    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PosSelectedItems(
    modifier: Modifier = Modifier,
    index : Int,
    item : TransactionItems,
    onIncrease : () ->Unit,
    onDecreaseQuantity : () -> Unit
) {
    ListItem(
        text = {
            Text(
                "${item.name}",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        secondaryText = {
            Text(
                "${item.price?.toPhp()}",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold
                ))
        },
        trailing = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalIconButton(onClick = onDecreaseQuantity) {
                    Icon(
                        Icons.Filled.Remove,
                        contentDescription = "Remove"
                    )
                }
                Text("${item.quantity}", style = MaterialTheme.typography.titleSmall.copy())
                FilledTonalIconButton(onClick = onIncrease) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "ADD"
                    )
                }
            }
        }
    )
}

@Composable
fun PosTabs(
    modifier: Modifier = Modifier,
    state: PosState,
    events: (PosEvents) -> Unit
) {
    val categories = state.categoryList
    val selectedIndex = state.selectedCategoryIndex
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEachIndexed { index: Int, category: Category ->
            CategoryTabItem(
                index = index,
                selectedIndex = state.selectedCategoryIndex,
                category = category ,
                onClick = {
                    events.invoke(PosEvents.OnCategoryClick(it))
                }
            )
        }
    }
}