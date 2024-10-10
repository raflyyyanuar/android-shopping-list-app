package com.example.shoppinglistapp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

data class ShoppingItem(
    val id : Int,
    var name : String,
    var quantity : Int,
    var isEditing : Boolean = false,
)

@Composable
fun ShoppingListApp() {
    val sItems = remember { mutableStateListOf<ShoppingItem>() }
    val openAddItemDialog = remember { mutableStateOf(false) }

//    sItems.add(ShoppingItem(0, "Flower", 1))
//    sItems.add(ShoppingItem(1, "Lamborghini", 3))
//    sItems.add(ShoppingItem(2, "Mouse", 7))

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { openAddItemDialog.value = true },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text("Add item")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(sItems) { item ->
                Text("${item.name} = ${item.quantity}")
            }
        }
    }

    when {
        openAddItemDialog.value -> {
            AddItemDialog(
                onDismissRequest = { openAddItemDialog.value = false },
                onConfirmation = { name, quantity ->
                    openAddItemDialog.value = false
                    Log.d("INFO", "Added item $name with quantity $quantity")
                    sItems.add(ShoppingItem(0, name, quantity))
                },
            )
        }
    }
}

@Composable
fun AddItemDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String, Int) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Adding item")

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
                    TextButton(
                        onClick = {
                            if(name.isEmpty() || quantity.isEmpty()) {
                                Toast
                                    .makeText(
                                        context,
                                        "Name and/or quantity can not be empty!",
                                        Toast.LENGTH_SHORT)
                                    .show()
                            }
                            else if(quantity.toIntOrNull() == null || quantity.toInt() <= 0 ) {
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
                        Text("Add")
                    }
                    TextButton(
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
