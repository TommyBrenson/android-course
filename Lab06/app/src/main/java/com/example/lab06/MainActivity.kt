package com.example.lab06

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {
    private val MY_PERMISSIONS_REQUEST_LOCATION = 1
    private var coordsList: ArrayList<String> = arrayListOf()
    private lateinit var listAdapter: ArrayAdapter<String>

    private var locationManager: LocationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        var listView = findViewById<ListView>(R.id.coordsList)
        listAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, coordsList)
        listView.adapter = listAdapter

        validateInput()
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
                    findViewById<TextView>(R.id.gps_coords)
            }
            else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION)
            }
        }
        else {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 10000, 0.5f, locationListener)
            locationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 10000, 0.5f, locationListener)
            showInfo()
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            showInfo(location)
            updateList(location)
        }
        override fun onProviderDisabled(provider: String) { showInfo() }
        override fun onProviderEnabled(provider: String) { showInfo() }
        override fun onStatusChanged(provider: String, status: Int,
                                     extras: Bundle) { showInfo() }
    }

    @SuppressLint("SetTextI18n")
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

    fun updateList(location: Location? = null) {
        val list = findViewById<ListView>(R.id.coordsList)

        if (location != null) {
            coordsList.forEachIndexed { index, element ->
                val coord = element.split(" ")
                val locationB = Location("Custom location")
                locationB.longitude = coord[0].toDouble()
                locationB.latitude = coord[1].toDouble()

                val listItem = list[index]

                if (location.distanceTo(locationB) < 100f) {
                    listItem.setBackgroundColor(resources.getColor(R.color.pale_red))
                } else {
                    listItem.setBackgroundColor(Color.TRANSPARENT)
                }
            }
        }
    }

    fun onButtonClick() {
        val inputText = findViewById<EditText>(R.id.coordInput).text.toString()

        if (!coordsList.contains(inputText)) {
            coordsList.add(inputText)
            listAdapter.notifyDataSetChanged()
        }
    }

    fun validateInput() {
        val pattern = "(\\d{2}.\\d+\\s\\d{2}.\\d+)?".toRegex()
        val input = findViewById<EditText>(R.id.coordInput)
        val submitButton = findViewById<Button>(R.id.addCoord)
        val errorText = findViewById<TextView>(R.id.inputError)

        submitButton.setOnClickListener {
            onButtonClick()
        }

        input.addTextChangedListener {
            val text = it?.toString() ?: return@addTextChangedListener
            if (text.matches(pattern)) {
                submitButton.isEnabled = true
                submitButton.isClickable = true
                errorText.visibility = View.INVISIBLE
            } else {
                submitButton.isEnabled = false
                submitButton.isClickable = false
                errorText.visibility = View.VISIBLE
            }
        }
    }
}