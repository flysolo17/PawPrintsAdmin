package com.eutech.pawprints.products.presentation.product

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.eutech.pawprints.R
import com.eutech.pawprints.products.data.Category
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.shared.presentation.routes.ProductRouter
import com.eutech.pawprints.shared.presentation.utils.generateRandomNumber
import com.eutech.pawprints.ui.custom.PriceText


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    state: ProductState,
    events: (ProductEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state.errors != null) {
            Toast.makeText(context,state.errors,Toast.LENGTH_SHORT).show()
        }
    }
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(span = { GridItemSpan(     4) }) {
            ProductTopAppBar {
                events.invoke(ProductEvents.OnCreateProduct(navHostController))
            }
        }
        item(span = { GridItemSpan(4) }) {
            CategoryTabs(state = state, events = events)
        }

        val selectedIndex = state.selectedCategoryIndex
        val items =
            if (state.selectedCategoryIndex == 0) state.products
            else state.products.filter {it.categoryID == state.categoryList[selectedIndex].id}
        items(items, key = {it.id  ?: generateRandomNumber() }) {
            ProductCard(
                product = it,
                onClick = {
                   navHostController.navigate(ProductRouter.ViewProduct.navigate(it.id ?: ""))
                },
            )
        }
    }

}

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Products,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        elevation = CardDefaults.elevatedCardElevation(1.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                modifier = modifier
                    .fillMaxWidth()
                    .height(150.dp),
                model = product.image,
                contentScale = ContentScale.Crop,
                contentDescription = "${product.name}",
                placeholder = painterResource(R.drawable.product),
                error = painterResource(R.drawable.product)
            )
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${product.name}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
                PriceText(product = product)
            }

        }
    }
}

@Composable
fun CategoryTabs(
    modifier: Modifier = Modifier,
    state: ProductState,
    events: (ProductEvents) -> Unit
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
                    events.invoke(ProductEvents.OnCategoryClick(it))
                }
            )
        }
    }
}

@Composable
fun CategoryTabItem(
    modifier: Modifier  = Modifier,
    index: Int,
    selectedIndex : Int,
    category: Category,
    onClick: (Int) -> Unit
) {
    val cardColors = if (index == selectedIndex)
         CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
     else
         CardDefaults.cardColors(
            containerColor = Color.Gray,
            contentColor = MaterialTheme.colorScheme.onSurface
        )

    Card(
        modifier = modifier.clickable {
           onClick(index)
        },
        colors = cardColors
    ) {
        val weight = if (index == selectedIndex) FontWeight.Bold else FontWeight.Normal
        Text(
            text = "${category.name}",
            fontWeight = weight,
            fontSize = 12.sp,
            modifier = modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}


@Composable
fun ProductTopAppBar(
    modifier: Modifier = Modifier,
    onClick : () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Products", style = MaterialTheme.typography.titleLarge)
        Button(onClick = onClick ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Product")
                Text(text = "Create Product")
            }
        }
    }
}