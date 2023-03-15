package com.example.littlelemon

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
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
    var selectedCategory by rememberSaveable() {
        mutableStateOf("all")
    }

    var search by rememberSaveable() {
        mutableStateOf("")
    }

    var menu by rememberSaveable() { mutableStateOf(emptyList<MenuItemNetwork>()) }
    coroutineScope.launch {
        menu = getMenu(httpClient)
    }

    var menuItems = menu.map { menuItem ->
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

    menuItems = menuItems.filter { i->i.title.contains(search,true)
            &&(if (selectedCategory.equals("all")) true else i.category.equals(selectedCategory,true)) }

    Column(modifier = Modifier.fillMaxSize()) {

        RestaurantDetails(navController)
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(color = LittleLemonGreen)
            .size(80.dp),
            contentAlignment = Alignment.Center
        ) {
            TextField(
                value = search,
                onValueChange = { search = it },
                label = { Text(text = "Search") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(color = Color.White)
            )
        }
        Text(
            text = "ORDER FOR DELIVERY!",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp)
        )
        
        Box(modifier = Modifier.fillMaxWidth()) {
            MenuCategorySelector(
                categories = listOf("Starters", "Mains", "Desserts", "Drinks"),
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = if (selectedCategory == category) {
                        "all"
                    } else {
                        category
                    }
                }
            )
        }

        LazyColumn( contentPadding = PaddingValues(5.dp), modifier = Modifier.fillMaxHeight()) {
            items(menuItems.size) { item ->
                CardItemMenu(Item = menuItems[item],navController)
            }
        }
    }
}

@Composable
fun MenuCategorySelector(
    categories: List<String> ,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        categories.forEach { category ->
            CategoryButton(
                text = category,
                isSelected = category == selectedCategory,
                onClick = {
                    onCategorySelected(category)
                }
            )
        }
    }
}

@Composable
fun CategoryButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) LittleLemonYellow else Color.LightGray,
            contentColor = if (isSelected) Color.White else LittleLemonGreen
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun RestaurantDetails(navController:NavHostController){
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.37f)
        .background(color = LittleLemonGreen),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier
            .height(50.dp)
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(start = 180.dp,5.dp,5.dp,5.dp),
            horizontalArrangement = Arrangement.SpaceAround
            ) {
            Image(painter = painterResource(id = R.drawable.littlelemonlogook), contentDescription = "Logo", modifier = Modifier.padding(end = 100.dp))
            Button(onClick = { navController.navigate(Profile.route) }, colors = ButtonDefaults.buttonColors(Color.White)) {
                Image(painter = painterResource(id = R.drawable.user), contentDescription = "user-image")
            }
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
fun CardItemMenu(Item: MenuItem,navController: NavHostController) {
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


