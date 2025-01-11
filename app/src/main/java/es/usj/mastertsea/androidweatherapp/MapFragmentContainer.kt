package es.usj.mastertsea.androidweatherapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
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

    companion object {
        private const val ARG_CITY_NAME = "city_name"
        private const val ARG_LATITUDE = "latitude"
        private const val ARG_LONGITUDE = "longitude"
        private const val ARG_CONDITION = "condition"

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
        cityName = arguments?.getString(ARG_CITY_NAME) ?: ""
        latitude = arguments?.getString(ARG_LATITUDE) ?: ""
        longitude = arguments?.getString(ARG_LONGITUDE) ?: ""
        condition = arguments?.getString(ARG_CONDITION) ?: ""
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

        // Configura el texto en el cuadro
        val textViewLocationInfo = view.findViewById<TextView>(R.id.textViewLocationInfo)
        textViewLocationInfo.text = "Ubicación: $cityName\nLatitud: $latitude\nLongitud: $longitude"
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val lat = latitude?.toDoubleOrNull()
        val lng = longitude?.toDoubleOrNull()

        if (lat != null && lng != null) {
            val location = LatLng(lat, lng)

            val drawableRes = getConditionIcon(condition.toString())

            val scaledIcon = BitmapDescriptorFactory.fromBitmap(
                resizeBitmap(drawableRes, 100, 100)
            )

            val markerOptions = MarkerOptions()
                .position(location)
                .title("$cityName - $condition")
                .icon(scaledIcon)

            googleMap.addMarker(markerOptions)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
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



}
