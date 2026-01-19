package com.avdhootsolutions.aswack_shopkeeper.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task

import kotlinx.android.synthetic.main.activity_maps1.*

import java.util.*


class MapActivity : FragmentActivity(), OnMapReadyCallback, LocationListener,GoogleApiClient.ConnectionCallbacks {

    private var mMap: GoogleMap? = null
    var currentLocation: Location? = null
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient

    val REQUEST_CODE = 101

    lateinit var addresses: List<Address>

    var selectedLatitude: Double = 0.0
    var selectedLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps1)

        init()

        clickListner()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();



    }

    private fun clickListner() {
        tvSubmit.setOnClickListener {
            if (selectedLatitude != 0.0 && selectedLongitude != 0.0){
                val intent = Intent()
                intent.putExtra(IntentKeyEnum.LATITUDE.name,selectedLatitude.toString())
                intent.putExtra(IntentKeyEnum.LONGITUDE.name,selectedLongitude.toString())
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }

    }

    private fun init() {

    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latLng).
        title("Your Current Location").
        snippet("Click accurately on the map")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
        googleMap.addMarker(markerOptions).showInfoWindow()

        googleMap.addCircle(
            CircleOptions()
                .center(LatLng(latLng.latitude, latLng.longitude))
                .radius(30.00)
                .strokeColor(Color.parseColor("#00000000"))
                .fillColor(Color.parseColor("#40000000"))
        )


        addMarkerOnGoogleClick(googleMap)
    }


    /**
     * fetch current location
     */
    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener(object : OnSuccessListener<Location?>, OnMapReadyCallback {
            override fun onSuccess(location: Location?) {
                if (location != null) {
                    currentLocation = location
                    Toast.makeText(
                        applicationContext,
                        currentLocation?.latitude
                            .toString() + "" + currentLocation?.longitude,
                        Toast.LENGTH_SHORT
                    ).show()
                    val supportMapFragment =
                        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
                    supportMapFragment.getMapAsync(this@MapActivity)

                    currentLocation?.latitude?.let {
                        getAddress(it, currentLocation?.longitude!!)
                    }


                }
            }

            override fun onMapReady(p0: GoogleMap) {

            }

        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    override fun onLocationChanged(p0: Location?) {

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }


    /**
     * Get address from the latiude and longtitude
     */
    fun getAddress(latitude : Double,longitude : Double){

        val geoCoder: Geocoder = Geocoder(this@MapActivity, Locale.getDefault())

        try {
            addresses = geoCoder.getFromLocation(
                    latitude,
                    longitude,
                    1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            val address: String = addresses[0]
                    .getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()


            val locality = addresses[0].subLocality
            val city: String = addresses[0].locality
            val state: String = addresses[0].adminArea
            val country: String = addresses[0].countryName
            val postalCode: String = addresses[0].postalCode
            val knownName: String =
                    addresses[0].featureName // Only if available else return NULL


            Log.e("address" , address)
            Log.e("postalCode" , postalCode)
            Log.e("city" , city)


            selectedLatitude = latitude
            selectedLongitude = longitude

//            tvTitle.text = locality
//            tvAddress.text = address

        }catch (e:Exception){
         Log.e("exception",e.toString())
        }

    }


    /**
     * Add marker on google map by click
     */
    fun addMarkerOnGoogleClick(googleMap: GoogleMap){
        googleMap.setOnMapClickListener(OnMapClickListener { point ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(point).
            title("Your product address").
            snippet("Click accurately on the map")).showInfoWindow()


            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(point.latitude, point.longitude), 18f
                )
            )
            googleMap.addCircle(
                CircleOptions()
                    .center(LatLng(point.latitude, point.longitude))
                    .radius(30.00)
                    .strokeColor(Color.parseColor("#00000000"))
                    .fillColor(Color.parseColor("#40000000"))

            )

            getAddress(point.latitude, point.longitude)


        })
    }


}