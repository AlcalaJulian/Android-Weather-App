package es.usj.mastertsea.androidweatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.usj.mastertsea.androidweatherapp.data.DayWeatherAdapter
import es.usj.mastertsea.androidweatherapp.data.HourlyWeatherAdapter
import es.usj.mastertsea.androidweatherapp.data.WeatherCurrentAdapter
import es.usj.mastertsea.androidweatherapp.data.WeatherViewModel
import es.usj.mastertsea.androidweatherapp.domain.model.HourlyWeather
import es.usj.mastertsea.androidweatherapp.domain.model.WeatherDay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WeatherDetailFragment : Fragment() {
    private val viewModel: WeatherViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewHours: RecyclerView
    private lateinit var recyclerViewDays: RecyclerView

    private lateinit var adapter: WeatherCurrentAdapter
    private lateinit var adapterHourly: HourlyWeatherAdapter
    private lateinit var adapterDay: DayWeatherAdapter

    private lateinit var imgTap: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewCity)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = viewModel.selectedWeather.value?.let { WeatherCurrentAdapter(city = it) }!!
        recyclerView.adapter = adapter

        recyclerViewHours = view.findViewById(R.id.recyclerViewHours)
        recyclerViewHours.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        adapterHourly = HourlyWeatherAdapter { hourlyWeather -> onClickHour(hourlyWeather) }
        recyclerViewHours.adapter = adapterHourly

        recyclerViewDays = view.findViewById(R.id.recyclerViewDays)
        recyclerViewDays.layoutManager = LinearLayoutManager(requireContext())

        adapterDay = DayWeatherAdapter { dayWeather -> onClickDay(dayWeather) }
        recyclerViewDays.adapter = adapterDay

        viewModel.selectedWeather.observe(viewLifecycleOwner) {
            it.let {
                val weather = it.weather.first()
                adapterHourly.submitList(weather.hourly)
                adapterDay.submitList(it.weather.filter {w -> w != weather})

                val textDay: TextView = view.findViewById(R.id.textDay)
                val textDate: TextView = view.findViewById(R.id.textDate)
                textDay.text = convertStringToDateAndGetDayOfWeek(weather.day, "yyyy-MM-dd")
                textDate.text = weather.day
            }
        }

        imgTap = view.findViewById(R.id.imgTap)
        imgTap.setOnClickListener {
            handleImageTapAction()
        }
    }
    private fun handleImageTapAction() {
        imgTap.alpha = 0.5f
        imgTap.postDelayed({
            imgTap.alpha = 1.0f
        }, 200)

        val cityName = viewModel.selectedWeather.value?.city ?: "Ciudad Desconocida"
        val locationCoordinatesLatitude = viewModel.selectedWeather.value?.location?.latitude?.toString() ?: ""
        val locationCoordinatesLongitude = viewModel.selectedWeather.value?.location?.longitude?.toString() ?: ""
        val condition = viewModel.selectedWeather.value?.weather?.first()?.hourly?.first()?.condition ?: ""

        val mapFragment = FragmentMapContainer.newInstance(cityName, locationCoordinatesLatitude, locationCoordinatesLongitude, condition)

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, mapFragment)
            .addToBackStack(null)
            .commit()
    }




    private fun onClickHour(hourlyWeather: HourlyWeather) {
        Toast.makeText(
            this.context,
            "Temperature: ${hourlyWeather.temperature}, Humity: ${hourlyWeather.humidity}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onClickDay(dayWeather: WeatherDay) {
        Toast.makeText(
            this.context,
            "Day: ${dayWeather.day}, Max: ${dayWeather.max}",
            Toast.LENGTH_SHORT
        ).show()
    }

}
fun convertStringToDateAndGetDayOfWeek(dateString: String, format: String): String {
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    val date = simpleDateFormat.parse(dateString)
    val calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
    }
    val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
    return dayOfWeek ?: "Unknown"
}
