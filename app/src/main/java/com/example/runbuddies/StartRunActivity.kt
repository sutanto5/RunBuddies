package com.example.runbuddies

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Button
import android.os.Bundle
import com.example.runbuddies.R
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import com.example.runbuddies.HomePageActivity
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.example.runbuddies.Run
import com.example.runbuddies.LogInActivity
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
import java.lang.Runnable
import java.lang.ref.WeakReference
import java.text.DecimalFormat
import java.util.*

class StartRunActivity : AppCompatActivity(), LocationListener {
    var decimalFormat = DecimalFormat(".##")

    // Use seconds, running and wasRunning respectively
    // to record the number of seconds passed,
    // whether the stopwatch is running and
    // whether the stopwatch was running
    // before the activity was paused.
    // Number of seconds displayed
    // on the stopwatch.
    private var seconds = 0
    private var distance = 0.0
    private var pace = 0

    // Is the stopwatch running?
    private var running = false
    private var wasRunning = false
    var runDistance: TextView? = null
    var runTime: TextView? = null
    var runPace: TextView? = null
    var start: Button? = null
    var stop: Button? = null
    var save: Button? = null
    var reset: Button? = null
    var paceTV: TextView? = null
    var timeTV: TextView? = null
    var toMap: Button? = null
    var toStats: Button? = null
    var map: MapView? = null
    //for location tracking
    private var latLong: TextView? = null
    private var address: TextView? = null
    private var distanceView: TextView? = null
    protected var latitude = 0.0
    protected var longitude = 0.0
    private var locationManager: LocationManager? = null
    private var last: Location? = null
    //private var distance: Long = 0

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
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_run)
        if (savedInstanceState != null) {

            // Get the previous state of the stopwatch
            // if the activity has been
            // destroyed and recreated.
            seconds = savedInstanceState
                .getInt("seconds")
            running = savedInstanceState
                .getBoolean("running")
            wasRunning = savedInstanceState
                .getBoolean("wasRunning")
        }
        runTimer()
        runTime = findViewById(R.id.time_view)
        runPace = findViewById(R.id.pace_View)
        runDistance = findViewById(R.id.milesView)
        start = findViewById(R.id.startButton)
        stop = findViewById(R.id.stopButton)
        save = findViewById(R.id.save)
        reset = findViewById(R.id.reset)
        paceTV = findViewById(R.id.paceTV)
        timeTV = findViewById(R.id.TIMETV)
        map = findViewById(R.id.mapView)
        setContentView(R.layout.activity_start_run)
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
        distanceView = findViewById(R.id.distanceTV)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager


    }

    // Save the state of the stopwatch
    // if it's about to be destroyed.
    public override fun onSaveInstanceState(
        savedInstanceState: Bundle
    ) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState
            .putInt("seconds", seconds)
        savedInstanceState
            .putBoolean("running", running)
        savedInstanceState
            .putBoolean("wasRunning", wasRunning)
    }

    // If the activity is paused,
    // stop the stopwatch.
    override fun onPause() {
        super.onPause()
        wasRunning = running
        running = false
    }

    // If the activity is resumed,
    // start the stopwatch
    // again if it was running previously.
    override fun onResume() {
        super.onResume()
        if (wasRunning) {
            running = true
        }
    }

    // Start the stopwatch running
    // when the Start button is clicked.
    // Below method gets called
    // when the Start button is clicked.
    fun onClickStart(view: View?) {
        running = true
        start!!.visibility = View.GONE
        start!!.text = "Resume"
        reset!!.visibility = View.INVISIBLE
        save!!.visibility = View.INVISIBLE
        stop!!.visibility = View.VISIBLE
    }

    // Stop the stopwatch running
    // when the Stop button is clicked.
    // Below method gets called
    // when the Stop button is clicked.
    fun onClickStop(view: View?) {
        running = false
        reset!!.visibility = View.VISIBLE
        stop!!.visibility = View.GONE
        start!!.visibility = View.VISIBLE
        save!!.visibility = View.VISIBLE
    }

    // Reset the stopwatch when
    // the Reset button is clicked.
    // Below method gets called
    // when the Reset button is clicked.
    fun onClickReset(view: View?) {
        running = false
        seconds = 0
        pace = 0
        distance = 0.0
        val intent = Intent(this@StartRunActivity, HomePageActivity::class.java)
        startActivity(intent)
    }

    fun addRun(view: View?) {
        start!!.visibility = View.GONE
        stop!!.visibility = View.GONE
        save!!.visibility = View.GONE
        reset!!.visibility = View.GONE
        distance!!.visibility = View.GONE
        paceTV!!.visibility = View.GONE
        timeTV!!.visibility = View.GONE
        runDistance!!.visibility = View.GONE
        runTime!!.visibility = View.GONE
        runPace!!.visibility = View.GONE

        val nameTV = findViewById<View>(R.id.nameTV)
        val name = findViewById<EditText>(R.id.runNameEditText)
        val upload = findViewById<Button>(R.id.upload)
        name.visibility = View.VISIBLE
        nameTV.visibility = View.VISIBLE
        upload.visibility = View.VISIBLE
    }

    fun switchToMap(view: View?) {
        distance!!.visibility = View.GONE
        paceTV!!.visibility = View.GONE
        timeTV!!.visibility = View.GONE
        runDistance!!.visibility = View.GONE
        runTime!!.visibility = View.GONE
        runPace!!.visibility = View.GONE

        toMap!!.visibility = View.GONE
        toStats!!.visibility = View.VISIBLE
        map!!.visibility = View.VISIBLE
    }

    fun switchToStats(view: View?) {
        toMap!!.visibility = View.VISIBLE
        toStats!!.visibility = View.GONE
        map!!.visibility = View.GONE
        distance!!.visibility = View.VISIBLE
        paceTV!!.visibility = View.VISIBLE
        timeTV!!.visibility = View.VISIBLE
        runDistance!!.visibility = View.VISIBLE
        runTime!!.visibility = View.VISIBLE
        runPace!!.visibility = View.VISIBLE
    }

    fun addRunButtonClicked(view: View?) {
        val nameET = findViewById<EditText>(R.id.runNameEditText)
        val name = nameET.text.toString()
        val dist = runDistance!!.text.toString()
        val time = runTime!!.text.toString()
        val pace = runPace!!.text.toString()
        val c = Calendar.getInstance()
        val day = c[Calendar.DAY_OF_MONTH]
        val month = c[Calendar.MONTH]
        val year = c[Calendar.YEAR]
        val date = (month + 1).toString() + "/" + day + "/" + year
        val r = Run(date, dist, pace, time, name)
        LogInActivity.firebaseHelper.addRunData(r)
        onClickReset(view)
    }

    // Sets the NUmber of seconds on the timer.
    // The runTimer() method uses a Handler
    // to increment the seconds and
    // update the text view.
    private fun runTimer() {

        // Get the text view.
        val timeView = findViewById<TextView>(
            R.id.time_view
        )
        val paceView = findViewById<TextView>(
            R.id.pace_View
        )
        val distView = findViewById<TextView>(
            R.id.milesView
        )
        // Creates a new Handler
        val handler = Handler()

        // Call the post() method,
        // passing in a new Runnable.
        // The post() method processes
        // code without a delay,
        // so the code in the Runnable
        // will run almost immediately.
        handler.post(object : Runnable {
            override fun run() {
                //time stuff
                val hours = seconds / 3600
                val minutes = seconds % 3600 / 60
                val secs = seconds % 60
                pace = (seconds / distance).toInt()
                val paceHours = pace / 3600
                val paceMinutes = pace % 3600 / 60
                val paceSecs = pace % 60

                // Format the seconds into hours, minutes,
                // and seconds.
                val time: String
                time = if (hours > 0) {
                    String.format(
                        Locale.getDefault(),
                        "%d:%02d:%02d", hours,
                        minutes, secs
                    )
                } else {
                    String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        minutes, secs
                    )
                }
                var pace: String
                pace = if (paceHours > 0) {
                    String.format(
                        Locale.getDefault(),
                        "%d:%02d:%02d", paceHours,
                        paceMinutes, paceSecs
                    )
                } else {
                    String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        paceMinutes, paceSecs
                    )
                }
                pace += "/mi"

                // Set the text view text.
                timeView.text = time
                paceView.text = pace
                distView.text = decimalFormat.format(distance) + "mi"


                // If running is true, increment the
                // seconds variable.
                if (running) {
                    seconds++
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000)
            }
        })
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
                    this@StartRunActivity,
                    R.drawable.dropdown,
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
            //distance = location.distanceTo(last).toLong()
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
    /*
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

     */
/*
    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                if(x1 < x2) {
                    Intent i = new Intent(StartRunActivity.this, MapActivity.class);
                    startActivity(i);
                }
        }
        return false;
    }

 */
}