package es.usj.mastertsea.androidweatherapp.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.usj.mastertsea.androidweatherapp.R
import es.usj.mastertsea.androidweatherapp.domain.model.City
import es.usj.mastertsea.androidweatherapp.domain.model.HourlyWeather
import es.usj.mastertsea.androidweatherapp.domain.model.WeatherDay
import es.usj.mastertsea.androidweatherapp.domain.util.WeatherUtil.Companion.getCurrentHour
import es.usj.mastertsea.androidweatherapp.domain.util.WeatherUtil.Companion.getCurrentWeather
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherCurrentAdapter(private val city: City) :
    RecyclerView.Adapter<WeatherCurrentAdapter.WeatherViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_current_time, parent, false)
            return WeatherViewHolder(view)
        }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
            holder.bind(city)
        }

        inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val textCity: TextView = view.findViewById(R.id.textCity)
            private val textTemperature: TextView = view.findViewById(R.id.textTemperature)
            private val textCondition: TextView = view.findViewById(R.id.textCondition)
            private val textMax: TextView = view.findViewById(R.id.textMaxMin)
            private val img: ImageView = view.findViewById(R.id.imgTemperature)

            fun bind(city: City) {
                textCity.text = city.city
                val now = Date()
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Define the format
                val formattedDate = format.format(now) // Convert to string

                val currentWeather = getCurrentWeather(city.weather, formattedDate)
                val currentHour = getCurrentHour(currentWeather.hourly, formattedDate)

                "Max: ${currentWeather.hourly.maxOf { it.temperature }}°C - Min: ${currentWeather.hourly.minOf { it.temperature }}°C".also { textMax.text = it }

                textCondition.text = currentHour.condition

                currentHour.temperature.toString()
                    .also { textTemperature.text = buildString {
                        append(it)
                        append("°")
                    } }
                img.setImageResource(getConditionIcon(textCondition.text as String))
            }
        }
}
fun getConditionIcon(condition: String): Int {
    return when (condition.lowercase()) {
        "sunny" -> R.drawable.sunny
        "cloudy" -> R.drawable.cloud
        "rainy" -> R.drawable.rainy
        else -> R.drawable.cloudy
    }
}