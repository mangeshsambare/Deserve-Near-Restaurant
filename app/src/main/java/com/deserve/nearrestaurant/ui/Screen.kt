package com.deserve.nearrestaurant.ui

sealed class Screen(val route: String) {

    object LogIn: Screen("log_in")
    object Restaurant: Screen("restaurant")
}