package com.deserve.nearrestaurant.util

import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

object AppPermissionUtil {

    const val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    const val REQUEST_CHECK_SETTINGS = 2

    fun checkLocationAccessGranted(activity: Activity, locationAccessGranted: () -> Unit) {
        if (!isLocationPermissionGranted(activity)) {
            requestLocationPermissions(activity)
        } else {
            checkLocationSettings(activity, locationAccessGranted)
        }
    }

    private fun isLocationPermissionGranted(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestLocationPermissions(activity: Activity) {
        when {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.Q -> {
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    activity.requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ), REQUEST_PERMISSIONS_REQUEST_CODE
                    )
                } else {
                    activity.requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ), REQUEST_PERMISSIONS_REQUEST_CODE
                    )
                }
            }
            Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> {
                activity.requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ), REQUEST_PERMISSIONS_REQUEST_CODE
                )
            }

            else -> {
                activity.requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), REQUEST_PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    fun checkLocationSettings(activity: Activity, locationAccessGranted: () -> Unit) {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val task = LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            val locationSettingsStates = locationSettingsResponse.locationSettingsStates
            locationSettingsStates?.let {
                if (it.isLocationPresent) {
                    locationAccessGranted()
                }
            }
        }

        task.addOnFailureListener {
            try {
                // Cast to a resolvable exception.
                val resolvable = it as ResolvableApiException
                resolvable.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
            } catch (ie: IntentSender.SendIntentException) {
                // Ignore the error.
            } catch (ce: ClassCastException) {
                // Ignore, should be an impossible error.
            }
        }
    }
}