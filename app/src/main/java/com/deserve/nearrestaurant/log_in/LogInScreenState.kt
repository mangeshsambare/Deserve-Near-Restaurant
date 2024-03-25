package com.deserve.nearrestaurant.log_in

data class LogInScreenState(
    val isLoading: Boolean = false,
    val logInStatus: Boolean = false,
    val error: String? = null
)
