package com.mog.bondoman.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class LocationProvider private constructor(context: Context) {
    companion object {
        private const val REQUESTPERMISSIONCODE = 69

        private var INSTANCE: LocationProvider? = null

        fun getInstance(context: Context): LocationProvider {
            return INSTANCE ?: LocationProvider(context.applicationContext)
                .also { INSTANCE = it }
        }
    }

    private val fusedLocationProviderClient: FusedLocationProviderClient
    private val currentLocationRequest: CurrentLocationRequest

    init {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context.applicationContext)
        currentLocationRequest = CurrentLocationRequest.Builder().build()
    }

    fun getCurrentLocation(
        context: Context,
        activity: Activity,
        locationListener: MutableLiveData<Location>
    ) {
        val isLocationPermissionAllowed = checkForPermission(context, activity)
        if (!isLocationPermissionAllowed) return

//        var receivedLocation: Location? = null
        fusedLocationProviderClient.getCurrentLocation(
            currentLocationRequest, null
//            object : CancellationToken() {
//                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
//                    CancellationTokenSource().token
//
//                override fun isCancellationRequested() = false
//            }
        )
            .addOnSuccessListener { location: Location? ->
                if (location == null) {
                    Log.d("LocationProvider", "location cannot be retrieved")
                    Toast.makeText(context, "Can't get location right now", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    locationListener.postValue(location)
                }
            }
    }

    // Thanks for check permission code https://alexportillo0519.medium.com/fused-location-provider-in-kotlin-47350c346ef3
    fun checkForPermission(context: Context, activity: Activity): Boolean {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // location access allowed
            return true
        } else {
            // if not, try requesting permission
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUESTPERMISSIONCODE
            )
            // then return the user's permission
            return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
        }
    }

//    private val locationPermissionRequest = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        when {
//            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
//                // Precise location access granted.
//            }
//
//            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
//                // Only approximate location access granted.
//            }
//
//            else -> {
//                // No location access granted.
//            }
//        }
//    }

    //    fun checkLocationPermission() {
//        locationPermissionRequest.launch(
//            arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//            )
//        )
//    }
}