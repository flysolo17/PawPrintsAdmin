package com.eutech.pawprints.appointments.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.eutech.pawprints.products.data.products.Products
import com.eutech.pawprints.products.presentation.product.ProductCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDialog(
    modifier: Modifier = Modifier,
    products : List<Products>,
    onSelected : (Products) -> Unit
) {
    var searchState by remember {
        mutableStateOf("")
    }
    val filteredProducts = products.filter {
        it.name!!.contains(searchState)
    }
    var dialog by remember {
        mutableStateOf(false)
    }
    if (dialog) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { dialog = !dialog },
            content = {
                //val dialogWidth = if (maxWidth > 1000.dp) 1000.dp else maxWidth * 0.9f
                Surface(
                    modifier = modifier
                        .fillMaxWidth(0.8f)
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(8.dp)
                ){
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = "Select Product")
                        TextField(
                            value = searchState,
                            onValueChange = {searchState = it},
                            modifier = modifier
                                .fillMaxWidth(),
                            label = {
                                Text(text = "Search name here....")
                            },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp),
                            trailingIcon = {
                                IconButton(
                                    modifier = modifier.size(24.dp),
                                    onClick = { searchState = "" })
                                {
                                    Icon(imageVector = Icons.Rounded.Clear, contentDescription = "Clear")
                                }
                            }
                        )
                        Spacer(modifier =modifier.height(12.dp))
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredProducts, key = {it.id!!}) {
                                ProductCard(product = it) {
                                    onSelected(it)
                                }
                            }
                        }
                    }
                }


            }
        )
    }

    FilledIconButton(
        shape = RoundedCornerShape(4.dp),
        onClick = { dialog = !dialog }
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Poduct")
    }
}


@Composable
fun ProductItemCard(
    modifier: Modifier = Modifier,
    it : Products,
    onSelected: (Products) -> Unit
) {

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        
    }
}