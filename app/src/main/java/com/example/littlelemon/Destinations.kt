package com.example.littlelemon

interface Destinations {
    val route:String
}
object Home:Destinations{
    override val route = "Home"
}

object  Onboarding: Destinations{
    override val route = "Onboardign"
}

object  Profile: Destinations{
    override val route = "Profile"
}

object  DishInformation: Destinations{
    override val route = "DishInformation"
}