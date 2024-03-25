package com.deserve.nearrestaurant

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deserve.nearrestaurant.ui.Screen
import com.deserve.nearrestaurant.ui.screen.LogInScreen
import com.deserve.nearrestaurant.ui.screen.RestaurantsScreen
import com.deserve.nearrestaurant.ui.theme.DeserveNearRestaurantTheme
import com.deserve.nearrestaurant.util.AppPermissionUtil
import com.deserve.nearrestaurant.util.LocationObservable
import com.deserve.nearrestaurant.util.LocationUtil

class MainActivity : ComponentActivity(), LocationObservable {
    private lateinit var viewModel: RestaurantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RestaurantViewModel::class.java]
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.LogIn.route
            ) {
                composable(
                    route = Screen.LogIn.route
                ) {
                    DeserveNearRestaurantTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            LogInScreen(viewModel = viewModel, logIn = {
                                viewModel.updateApiKey(it)
                                navController.navigate(Screen.Restaurant.route)
                            })
                        }
                    }
                }
                composable(route = Screen.Restaurant.route) {
                    DeserveNearRestaurantTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            RestaurantsScreen(
                                restaurantViewModel = viewModel,
                                navController = navController
                                )
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        AppPermissionUtil.checkLocationAccessGranted(this) {
            observeLocation()
        }
    }

    override fun onResume() {
        super.onResume()
        observeLocation()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdate()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AppPermissionUtil.REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isEmpty()) {
//                provideLocationAccess()
            } else {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                    if (grantResults.size > 1 && PackageManager.PERMISSION_GRANTED == grantResults[1]) {
                        checkLocationSettings()
                    }
                } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            ), AppPermissionUtil.REQUEST_PERMISSIONS_REQUEST_CODE
                        )
                    }
                } else if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    checkLocationSettings()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AppPermissionUtil.REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    RESULT_OK -> {
                        observeLocation()
                    }
                    RESULT_CANCELED -> checkLocationSettings()
                }
            }
        }
    }

    override fun onLocationChange(location: Location?) {
        location?.let {
            viewModel.updateLocation(location)
        }
    }

    private fun checkLocationSettings() {
        AppPermissionUtil.checkLocationSettings(this) {
            observeLocation()
        }
    }

    private fun observeLocation() {
        LocationUtil.addObservable(this)
        LocationUtil.startLocation(this)
    }

    private fun stopLocationUpdate() {
        LocationUtil.removeObservable(this)
        LocationUtil.stopLocation(this)
    }
}