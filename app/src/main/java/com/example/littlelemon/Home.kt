package com.example.littlelemon

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.littlelemon.ui.theme.LittleLemonGreen
import com.example.littlelemon.ui.theme.LittleLemonYellow
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


private suspend fun getMenu(httpClient: HttpClient):List<MenuItemNetwork>{
        val menuResponse : MenuNetworkdata = httpClient
            .get("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json")
            .body()
        println("getMenu(): ${menuResponse}" )
        return menuResponse.menu
    }

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Home(navController: NavHostController,context: Context) {

    val coroutineScope = rememberCoroutineScope()
    val httpClient = HttpClient (Android){
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    var search by rememberSaveable() {
        mutableStateOf("")
    }

    var menu by rememberSaveable() { mutableStateOf(emptyList<MenuItemNetwork>()) }
    coroutineScope.launch {
        menu = getMenu(httpClient)
    }

    val menuItems = menu.map { menuItem ->
        MenuItem(
            id = menuItem.id,
            title = menuItem.title,
            description = menuItem.description,
            image = menuItem.image,
            price = menuItem.price,
            category = menuItem.category
        )
    }

    coroutineScope.launch {
        val menuDatabase = MenuDatabase.getInstance(context = context)
        menuDatabase.menuDao().insertAll(menuItems)
    }


    Column(modifier = Modifier.fillMaxSize()) {

        RestaurantDetails()
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(color= LittleLemonGreen)
            .size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text(text = "Search") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(0.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
            )
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
fun RestaurantDetails(){
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.4f)
        .background(color = LittleLemonGreen),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier
            .height(50.dp)
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.Center
            ) {
            Image(painter = painterResource(id = R.drawable.littlelemonlogook), contentDescription = "Logo")
        }
        Column() {
            Text(text = "Little Lemon",
                fontSize = 36.sp,
                color = LittleLemonYellow,
                modifier = Modifier.padding(start= 25.dp, top = 15.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column() {
                    Text(text = "Chicago",
                        fontSize = 24.sp,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Text(text = "We are a family owned Mediterranean restaurant," +
                            " focused on traditional recipes served with a modern twist.",
                        color=Color.White,
                        modifier = Modifier.fillMaxWidth(0.5f),
                        fontSize = 16.sp
                    )
                }

                Image(painter = painterResource(id = R.drawable.image), contentDescription = "Introduction")
            }
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
                        .clip(shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)),// set contentDescription to null if image is decorative
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


@Preview
@Composable
fun RestaurantDetailsPreview(){
    RestaurantDetails()
}

