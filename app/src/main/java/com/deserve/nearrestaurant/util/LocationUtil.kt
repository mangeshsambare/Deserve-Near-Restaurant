package com.deserve.nearrestaurant.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

internal object LocationUtil {

    private const val INTERVAL_TIME = 60000L
    private const val FASTEST_INTERVAL_TIME = 60000L
    private const val SMALLEST_DISPLACEMENT = 1000F
    private var location: Location? = null
    private val OBSERVABLE_IMPLS: MutableList<LocationObservable> = mutableListOf()
    private val locationRequest = LocationRequest.create().apply {
        interval = INTERVAL_TIME
        fastestInterval = FASTEST_INTERVAL_TIME
        smallestDisplacement = SMALLEST_DISPLACEMENT
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private var initialized = false

    private var locationUpdateListener = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            setLocationAndNotifyObservables(locationResult.lastLocation)
        }
    }

    @Synchronized
    fun startLocation(context: Context) {
        if (!initialized && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLastLocation(context)
            LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(locationRequest, locationUpdateListener, Looper.getMainLooper())
            initialized = true
        }
    }

    @Synchronized
    fun stopLocation(context: Context) {
        if (initialized && OBSERVABLE_IMPLS.isEmpty()) {
            LocationServices.getFusedLocationProviderClient(context)
                .removeLocationUpdates(locationUpdateListener)
            initialized = false
        }
    }

    private fun getLocation(): Location? {
        return location
    }

    fun addObservable(locationObservable: LocationObservable): LocationObservable {
        OBSERVABLE_IMPLS.add(locationObservable)
        locationObservable.onLocationChange(getLocation())
        return locationObservable
    }

    fun removeObservable(locationObservable: LocationObservable) {
        OBSERVABLE_IMPLS.remove(locationObservable)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(context: Context) {
        LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener {
            setLocationAndNotifyObservables(it)
        }
    }

    @Synchronized
    private fun setLocationAndNotifyObservables(location: Location?) {
        LocationUtil.location = location
        for (observable in OBSERVABLE_IMPLS) {
            observable.onLocationChange(LocationUtil.location)
        }
    }

}

interface LocationObservable {
    fun onLocationChange(location: Location?)
}