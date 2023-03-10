package com.example.lab06

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_LOCATION = 1

    private var locationManager: LocationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    override fun onResume() {
        super.onResume()
        startTracking()
    }
    override fun onPause() {
        super.onPause()
        stopTracking()
    }

    fun startTracking() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            }
            else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION)
            }
        }
        else {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 10f, locationListener)
            locationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000, 10f, locationListener)
            showInfo()
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) { showInfo(location) }
        override fun onProviderDisabled(provider: String) { showInfo() }
        override fun onProviderEnabled(provider: String) { showInfo() }
        override fun onStatusChanged(provider: String, status: Int,
                                     extras: Bundle) { showInfo() }
    }

    private fun showInfo(location: Location? = null) {
        val isGpsOn = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkOn = locationManager!!.
        isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        findViewById<TextView>(R.id.gps_status).text =
            if (isGpsOn) "GPS ON" else "GPS OFF"
        findViewById<TextView>(R.id.network_status).text =
            if (isNetworkOn) "Network ON" else "Network OFF"
        if (location != null) {
            if (location.provider == LocationManager.GPS_PROVIDER) {
                findViewById<TextView>(R.id.gps_coords).text =
                    "GPS: широта = " + location.latitude.toString() +
                            ", долгота = " + location.longitude.toString()
            }
            if (location.provider == LocationManager.NETWORK_PROVIDER) {
                findViewById<TextView>(R.id.network_coords).text =
                    "Network: широта = " + location.latitude.toString() +
                            ", долгота = " + location.longitude.toString()
            }
        }
    }

    fun stopTracking() {
        locationManager!!.removeUpdates(locationListener)
    }

    fun buttonOpenSettings(view: View) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        startActivity(intent)
    }
}