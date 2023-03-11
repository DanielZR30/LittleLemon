package com.example.littlelemon

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.ui.theme.LittleLemonGreen
import com.example.littlelemon.ui.theme.LittleLemonYellow

@Composable
fun Profile(navController: NavHostController,context: Context) {
    var data:Array<String>

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier= Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp), contentAlignment = Alignment.Center){
            Image(
                painter = painterResource(id = R.drawable.littlelemonlogook),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(100.dp)
            )
        }
        Box(modifier = Modifier
            .height(100.dp)
            .background(color = LittleLemonGreen), contentAlignment = Alignment.Center) {
            Text(text = "Personal Information",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    color= Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp
                )
            )
        }
        Box(modifier = Modifier.height(300.dp) ) {
            Form(
                context,
                textButton = "Update",
                onClickAction = ::saveData,
                navController = navController
            )
        }
        Box(modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 50.dp, start = 20.dp, end = 20.dp), contentAlignment = Alignment.BottomCenter) {
            Button(
                onClick = {
                        saveData(null, null, null, context)
                        navController.navigate(Onboarding.route)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = LittleLemonYellow),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Log out")
            }
        }
    }

}
