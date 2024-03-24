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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
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
            DeserveNearRestaurantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RestaurantsScreen(restaurantViewModel = viewModel)
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
            viewModel.findRestaurant(it.latitude.toString(), it.longitude.toString())
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DeserveNearRestaurantTheme {
        Greeting("Android")
    }
}