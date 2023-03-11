package com.example.littlelemon

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
fun Onboarding(navController: NavHostController, context: Context) {

    var data: Array<String>

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.littlelemonlogook),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(100.dp)
            )
        }
        Box(
            modifier = Modifier
                .height(100.dp)
                .background(color = LittleLemonGreen), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Let's get know you",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp
                )
            )
        }
        Text(text = "Personal Information:", fontSize = 20.sp, modifier = Modifier.padding(top = 10.dp))
        Form(context = context, textButton = "Register", onClickAction = ::saveData, navController = navController)

    }
}

@Composable
fun AlertDialogWithText(text: String, context: Context) {
    var openDialog = remember { mutableStateOf(false) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "Alert Dialog Title") },
            text = { Text(text = text) },
            confirmButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(text = "OK")
                }
            }
        )
    }
    Button(onClick = { openDialog.value = true }) {
        Text(text = "Show Alert Dialog")
    }
}

@Composable
fun Form(context: Context, textButton:String, navController: NavHostController,onClickAction: (String?,String?,String?,Context)->Boolean) {

    val pref: SharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    var email: String by rememberSaveable() {
        mutableStateOf(pref.getString("email", "").toString())
    }
    var firstName: String by rememberSaveable() {
        mutableStateOf(pref.getString("firstName", "").toString())
    }
    var lastName: String by rememberSaveable() {
        mutableStateOf(pref.getString("lastName", "").toString())
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(text = "First Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(text = "Last Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = {
                if (validateEmail(email) && validateName(firstName)&& validateName(lastName)) {
                    onClickAction(firstName, lastName, email, context)
                    navController.navigate(Home.route)
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = LittleLemonYellow),
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            Text(text = textButton)
        }


    }
}

@Composable
fun BoxForm(label: String? = "Hola", input: String):String{
    var value by rememberSaveable(){
        mutableStateOf(input)
    }
    println("aquui"+input);
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 10.dp)
        ){
        //Text(text = label.toString(), fontSize = 12.sp)
        OutlinedTextField(value = value,
            onValueChange = {value = it},
            label = { Text(text = label.toString()) },
            modifier =  Modifier.fillMaxWidth()
        )
    }
    return value
}

fun saveData(firstName: String?, lastName:String?, email: String?, context: Context):Boolean{
    val pref: SharedPreferences = context.getSharedPreferences("Data",Context.MODE_PRIVATE)
    val editor = pref.edit()
    editor.putString("firstName", firstName)
    editor.putString("lastName", lastName)
    editor.putString("email", email)
    editor.apply() // use apply instead of commit for better performance
    return true
}

fun validateName(name: String?):Boolean{
    if(name != null){
        return name.length > 2
    }
    return false
}

fun validateEmail(email: String?):Boolean{
    if (email != null) {
        return email.contains("@") && email.contains(".")
    }
    return false;
}