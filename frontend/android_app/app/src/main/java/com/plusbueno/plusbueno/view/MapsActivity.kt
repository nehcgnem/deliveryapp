package com.plusbueno.plusbueno.view

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.plusbueno.plusbueno.R
import com.plusbueno.plusbueno.data.ServerUserLocation
import com.plusbueno.plusbueno.parser.LoginManager
import com.plusbueno.plusbueno.parser.UniversalParser
import com.plusbueno.plusbueno.parser.util.HttpUtil
import com.plusbueno.plusbueno.presenter.MapsActivityPresenter
import java.lang.Exception
import java.security.Permission
import java.util.*
import kotlin.math.roundToInt

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener ,MapsActivityView {

    private lateinit var presenter: MapsActivityPresenter
    private lateinit var mMap: GoogleMap
    private lateinit var packageMarker: Marker
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var mOrderName: String
    private lateinit var locationManager : LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mOrderName = intent.getStringExtra("ORDER_NAME")
        presenter = MapsActivityPresenter(this, mOrderName)

        // Obtain a position. Do nothing if unavailable
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                // Called when a new location is found by the network location provider.
                val task = SendCoordinateTask()
                task.execute((location.latitude * 10000).roundToInt(), (location.longitude * 10000).roundToInt())

            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            }

            override fun onProviderEnabled(provider: String) {
            }

            override fun onProviderDisabled(provider: String) {
            }
        }

        try {
            var lastKnownLocation: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null && (Date()).time -lastKnownLocation.time < 3.6e+6) {  // 1 hr
                val task = SendCoordinateTask()
                task.execute((lastKnownLocation.latitude * 10000).roundToInt(), (lastKnownLocation.longitude * 10000).roundToInt())
                Log.v("MAPCOORD", "Sent GPS " + lastKnownLocation.latitude + "," +lastKnownLocation.longitude )
            } else {
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (lastKnownLocation != null && (Date()).time -lastKnownLocation.time < 3.6e+6) {
                    val task = SendCoordinateTask()
                    task.execute((lastKnownLocation.latitude * 10000).roundToInt(), (lastKnownLocation.longitude * 10000).roundToInt())
                    Log.v("MAPCOORD", "Sent net " + lastKnownLocation.latitude + "," +lastKnownLocation.longitude )
                } else {
                    Log.e("MAPCOORD", "Cannot get last location")
                }
            }
        } catch (e : SecurityException) {
            Toast.makeText(this, "Please allow location", Toast.LENGTH_SHORT).show()
        } catch (e : Exception) {
            Log.e("MAPCOORD", e.message + e.toString())
        }


        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        handler = Handler()

        runnable = Runnable {
//            Log.v("RUNNABLE","I ran!")
            presenter.updateMap()
            handler.postDelayed(runnable, 5000)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val packagePos = LatLng(43.5, -80.5)
        packageMarker = mMap.addMarker(MarkerOptions().position(packagePos).title("Package Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(packagePos))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f))

        mMap.setOnMapClickListener(this)
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable, 1000)
    }

    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(runnable)
    }

    override fun updateMap(lat: Double, lng: Double) {
        val newPos = LatLng(lat / 10000, lng / 10000)
        packageMarker.position = newPos
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newPos))
    }

    override fun onMapClick(pos : LatLng) {
        updateMap(pos.latitude, pos.longitude)
    }

    private class SendCoordinateTask : AsyncTask<Int, String, String>() {
        override fun doInBackground(vararg params: Int?): String {
            val coord = ServerUserLocation("" + params[0] + "," + params[1])
            try {
                Log.v("SENDCOORD", coord.userCoord)
                HttpUtil.put(UniversalParser.BASE_URL_RESTFUL + "/profile/" + LoginManager.getUsername() + "/", coord, ServerUserLocation::class.java)
            } catch (e : Exception) {
                Log.e("SENDCOORD", e.message + e.toString())
            }
            return ""
        }
    }
}


