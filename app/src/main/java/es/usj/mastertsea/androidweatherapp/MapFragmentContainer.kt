package es.usj.mastertsea.androidweatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import es.usj.mastertsea.androidweatherapp.data.getConditionIcon

class FragmentMapContainer : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var cityName: String? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var condition: String? = null
    private lateinit var locationManager: LocationManager
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var currentLocation: LatLng? = null

    companion object {
        private const val ARG_CITY_NAME = "city_name"
        private const val ARG_LATITUDE = "latitude"
        private const val ARG_LONGITUDE = "longitude"
        private const val ARG_CONDITION = "condition"
        private const val REQUEST_CODE = 0

        fun newInstance(cityName: String, latitude: String, longitude: String, condition: String): FragmentMapContainer {
            val fragment = FragmentMapContainer()
            val args = Bundle()
            args.putString(ARG_CITY_NAME, cityName)
            args.putString(ARG_LATITUDE, latitude)
            args.putString(ARG_LONGITUDE, longitude)
            args.putString(ARG_CONDITION, condition)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cityName = arguments?.getString(ARG_CITY_NAME)
        latitude = arguments?.getString(ARG_LATITUDE)
        longitude = arguments?.getString(ARG_LONGITUDE)
        condition = arguments?.getString(ARG_CONDITION)
    }


    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        return inflater.inflate(R.layout.fragment_map_container, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val textViewLocationInfo = view.findViewById<TextView>(R.id.textViewLocationInfo)
        textViewLocationInfo.text = "Ubicación: $cityName\nLatitud: $latitude\nLongitud: $longitude"
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val lat = latitude?.toDoubleOrNull()
        val lng = longitude?.toDoubleOrNull()

        if (lat != null && lng != null) {
            currentLocation = LatLng(lat, lng)

            val drawableRes = getConditionIcon(condition.toString())

            val scaledIcon = BitmapDescriptorFactory.fromBitmap(
                resizeBitmap(drawableRes, 100, 100)
            )

            val markerOptions = MarkerOptions()
                .position(currentLocation!!)
                .title("$cityName - $condition")
                .icon(scaledIcon)

            googleMap.addMarker(markerOptions)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation!!, 12f))
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun resizeBitmap(drawableRes: Int, width: Int, height: Int): Bitmap {
        val drawable = resources.getDrawable(drawableRes, null)
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    private fun requestGrantLocation(): Boolean{
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)){
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE)
           return false
        }
        return true
    }

    // Get current location
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this.requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val currentLocation = LatLng(location.latitude, location.longitude)
                        googleMap.clear()
                        googleMap.addMarker(MarkerOptions().position(currentLocation))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16F))
                    }
                }
            } else {
                Toast.makeText(this.requireActivity(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    // Get current location, if shifted
    // from previous location
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    // If current location could not be located, use last location
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult.lastLocation != null){
                currentLocation = LatLng(locationResult.lastLocation!!.latitude, locationResult.lastLocation!!.longitude)
            }

        }
    }

    // function to check if GPS is on
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = this.requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // Check if location permissions are
    // granted to the application
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this.requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this.requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    // Request permissions if not granted before
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this.requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    // What must happen when permission is granted
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

}
