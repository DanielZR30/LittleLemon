package com.example.littlelemon

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable

@Composable
fun Navigation(context: Context){


    val navController = rememberNavController()
    val pref: SharedPreferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    val email = pref.getString("email", null)
    val firstName = pref.getString("firstName", null)
    val lastName = pref.getString("lastName", null)
    NavHost(navController= navController,startDestination= if (email != null && firstName != null && lastName != null) {
        Home.route
    } else {
        Onboarding.route
    }){
        composable(Onboarding.route){
            Onboarding(navController,context)
        }

        composable(Home.route){
            Home(navController,context)
        }
        composable(Profile.route){
            Profile(navController,context)
        }
        composable(DishInformation.route){
            DishInformation(navController = navController)
        }
    }
}


