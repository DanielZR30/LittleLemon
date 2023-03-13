package com.example.littlelemon

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.lifecycleScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


    private suspend fun getMenu(httpClient: HttpClient):List<MenuItemNetwork>{
        val menuResponse : MenuNetworkdata = httpClient
            .get("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json")
            .body()
        println("getMenu(): ${menuResponse}" )
        return menuResponse.menu
    }

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Home(navController: NavHostController) {

    val coroutineScope = rememberCoroutineScope()
    val httpClient = HttpClient (Android){
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    var menu by remember { mutableStateOf(emptyList<MenuItemNetwork>()) }
    coroutineScope.launch {
        menu = getMenu(httpClient)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Button(
            onClick = {
                coroutineScope.launch {
                    menu = getMenu(httpClient)
                }
            }
        ) {
            Text("Get Menu")
        }
        LazyColumn( contentPadding = PaddingValues(5.dp)) {
            items(menu.size) { item ->
                CardItemMenu(Item = menu[item],navController)
            }
        }
        println(menu.size)
        Button(onClick = { navController.navigate(Profile.route) }) {
            Text(text = "Go to Profile")
        }
    }
}


    @Composable
    fun CardItemMenu(Item: MenuItemNetwork,navController: NavHostController) {
        val modifier = Modifier
            .border(border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(10.dp))
            .height(100.dp)
            .clickable { navController.navigate("MenuItem") }
        val painter: Painter = rememberAsyncImagePainter(Item.image)

        Box(modifier = Modifier.padding(5.dp)) {
            Row(modifier = modifier) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .fillMaxHeight(),

                ) {
                    Image(
                        painter = painter,
                        contentDescription = Item.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape= RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)),// set contentDescription to null if image is decorative
                        contentScale = ContentScale.Crop
                    )
                }
                Box(modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)) {
                    Column() {
                        Text(text = Item.title, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        Text(
                            text = Item.description,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )

                    }
                }
            }
        }
    }

