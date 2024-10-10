package com.example.shoppinglistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.shoppinglistapp.ui.theme.ShoppingListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListAppTheme {
                ShoppingListApp()
            }
        }
    }
}

@Composable
fun ShoppingListApp() {

    val sItems = remember { mutableStateListOf<ShoppingItem>() }

    sItems.add(ShoppingItem(0, "Flower", 1))
    sItems.add(ShoppingItem(1, "Lamborghini", 3))
    sItems.add(ShoppingItem(2, "Mouse", 7))

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { /*TODO*/ },
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
}

data class ShoppingItem(
    val id : Int,
    var name : String,
    var quantity : Int,
    var isEditing : Boolean = false,
)
