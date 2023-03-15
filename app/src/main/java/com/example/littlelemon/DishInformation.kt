package com.example.littlelemon

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun DishInformation(navController: NavHostController){
    Column(modifier = Modifier.padding(10.dp)) {
        Text(text = "Sorry this page is not supported yet")
        Button(onClick = { navController.navigate("Home")}) {
            Text(text = "Go Home!")
        }
    }


}