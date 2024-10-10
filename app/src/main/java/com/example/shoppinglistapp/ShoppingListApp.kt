package com.example.shoppinglistapp

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

data class ShoppingItem(
    val id : Int,
    var name : String = "Name",
    var quantity : Int = 0,
)

@Composable
fun ShoppingListApp() {
    val sItems = remember { mutableStateListOf<ShoppingItem>() }
    val openAddItemDialog = remember { mutableStateOf(false) }
    val openEditItemDialog = remember { mutableStateOf(false) }

    var editedItem by remember { mutableStateOf(ShoppingItem(-1)) }
    var itemIdCounter by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { openAddItemDialog.value = true },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text("Add item")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sItems, key = { it.id }) { item ->
                ShoppingListItem(
                    item = item,
                    onEditClick = {
                        editedItem = item
                        openEditItemDialog.value = true
                    },
                    onDeleteClick = {
                        sItems.remove(item)
                    }
                )
            }
        }
    }

    when {
        openAddItemDialog.value -> {
            AddItemDialog(
                onDismissRequest = { openAddItemDialog.value = false },
                onConfirmation = { name, quantity ->
                    sItems.add(ShoppingItem(itemIdCounter, name, quantity))
                    itemIdCounter++
                    openAddItemDialog.value = false
                },
            )
        }
        openEditItemDialog.value -> {
            EditItemDialog(
                initialName = editedItem.name,
                initialQuantity = editedItem.quantity.toString(),
                onDismissRequest = { openEditItemDialog.value = false },
                onConfirmation = { name, quantity ->
                    val index = sItems.indexOfFirst { it.id == editedItem.id }
                    if (index != -1) {
                        sItems[index] = editedItem.copy(name = name, quantity = quantity)
                    }
                    openEditItemDialog.value = false
                },
            )
        }
    }
}

// Generic dialog to manipulate shopping list items
@Composable
fun ItemDialog(
    initialName : String = "",
    initialQuantity : String = "",
    onDismissRequest : () -> Unit,
    onConfirmation : (String, Int) -> Unit,
    dialogText : String = "This is a Dialogue"
) {
    var name by remember { mutableStateOf(initialName) }
    var quantity by remember { mutableStateOf(initialQuantity) }

    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(275.dp)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    dialogText,
                    style = TextStyle(fontSize = 16.sp, color = Color.Black),
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if(name.isEmpty() || quantity.isEmpty()) {
                                Toast
                                    .makeText(
                                        context,
                                        "Name and/or quantity can not be empty!",
                                        Toast.LENGTH_SHORT)
                                    .show()
                            }
                            else if((quantity.toIntOrNull() ?: 0) <= 0) {
                                Toast
                                    .makeText(
                                        context,
                                        "Invalid quantity!",
                                        Toast.LENGTH_SHORT)
                                    .show()
                            }
                            else {
                                onConfirmation(name, quantity.toInt())
                            }
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("OK")
                    }
                    Button(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}


@Composable
fun AddItemDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String, Int) -> Unit,
) {
    ItemDialog(
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation,
        dialogText = "Add Shopping Item"
    )
}

@Composable
fun EditItemDialog(
    initialName : String,
    initialQuantity : String,
    onDismissRequest: () -> Unit,
    onConfirmation: (String, Int) -> Unit,
) {
    ItemDialog(
        initialName = initialName,
        initialQuantity = initialQuantity,
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation,
        dialogText = "Edit Shopping Item"
    )
}

@Composable
fun ShoppingListItem(
    item : ShoppingItem,
    onEditClick : () -> Unit,
    onDeleteClick : () -> Unit,
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black, RoundedCornerShape(4.dp)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            item.name,
            modifier = Modifier.padding(8.dp),
        )
        Text(
            "Qty: ${item.quantity}",
            modifier = Modifier.padding(8.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, "Edit")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, "Delete")
            }
        }
    }
}
