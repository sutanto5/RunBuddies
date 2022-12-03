package com.example.runbuddies

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*


class MapActivity : AppCompatActivity(),LocationListener {
    //for location tracking
    private var latLong: TextView? = null
    private var address: TextView? = null
    private var distanceView: TextView? = null
    protected var latitude = 0.0
    protected var longitude = 0.0
    private var locationManager: LocationManager? = null
    private var last: Location? = null
    private var distance: Long = 0
    //for slide screen
    var x1 = 0f
    var x2 = 0f
    //for mapbox implementation
    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }
    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
    }
    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }
        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }
        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }
    //map
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        mapView = findViewById(R.id.mapView)
        mapView?.getMapboxMap()?.loadStyleUri(Style.SATELLITE_STREETS)
        //Mapbox Streets: Style.MAPBOX_STREETS
        //Mapbox Outdoors: Style.OUTDOORS
        //Mapbox Satellite: Style.SATELLITE
        //Mapbox Satellite Streets: Style.SATELLITE_STREETS
        //Mapbox Light: Style.LIGHT
        //Mapbox Dark: Style.DARK
        //Mapbox Traffic Day: Style.TRAFFIC_DAY
        //Mapbox Traffic Night: Style.TRAFFIC_NIGHT
        //Different map styles we can choose
        //We can also choose different style based on runtime
        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }
        latLong = findViewById(R.id.latLong)
        address = findViewById(R.id.address)
        distanceView = findViewById(R.id.distance)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }
    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            initLocationComponent()
            setupGesturesListener()
        }
    }
    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }
    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    this@MapActivity,
                    R.drawable.profileicon,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
    }
    @Throws(InterruptedException::class)
    fun getLocation(view: View?) {
        //if permission allowed retrieve location
        retrieveLocation()
    }
    @SuppressLint("MissingPermission")
    private fun retrieveLocation() {
        //min time is min time between requests and minDistance is distance needed till update
        locationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 500, 0f,
            this
        )
        val location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            latitude = location.latitude
            longitude = location.longitude
            //convert lat long to address
            val geocoder = Geocoder(this, Locale.getDefault())
            try {
                val addressList = geocoder.getFromLocation(latitude, longitude, 1)
                latLong!!.text = "Lat:$latitude \nLong: $longitude"
                address!!.text = addressList[0].getAddressLine(0)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    //Automatic
    override fun onLocationChanged(location: Location) {
        //convert lat long to address
        val geocoder = Geocoder(this, Locale.getDefault())
        latitude = location.latitude
        longitude = location.longitude
        try {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            latLong!!.text = "Latitude:$latitude \nLongitude: $longitude"
            address!!.text = addressList[0].getAddressLine(0)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (last != null) {
            distance += location.distanceTo(last).toLong()
        }
        last = Location(location)
        distanceView!!.text = "Distance: $distance meters"
        locationManager!!.removeUpdates(this)
    }
    private fun onCameraTrackingDismissed() {
        Toast.makeText(this, "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }
    override fun onDestroy() {
        super.onDestroy()
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            retrieveLocation()
        } else {
            latLong!!.text = "Permission Denied"
            address!!.text = "Permission Denied"
        }
    }
    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }
    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
    //switch screens
    override fun onTouchEvent(touchEvent: MotionEvent): Boolean {
        when (touchEvent.action) {
            MotionEvent.ACTION_DOWN -> x1 = touchEvent.x
            MotionEvent.ACTION_UP -> {
                x2 = touchEvent.x
                if (x1 > x2) {
                    val i = Intent(this@MapActivity, SaveRun::class.java)
                    startActivity(i)
                }
            }
        }
        return false
    }
}

