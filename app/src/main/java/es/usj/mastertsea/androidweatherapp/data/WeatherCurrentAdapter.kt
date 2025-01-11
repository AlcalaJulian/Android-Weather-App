package es.usj.mastertsea.androidweatherapp.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.usj.mastertsea.androidweatherapp.R
import es.usj.mastertsea.androidweatherapp.domain.model.City

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
                "Max: ${city.weather.first().hourly.maxOf { it.temperature }}° Min: ${city.weather.first().hourly.maxOf { it.temperature }}°".also { textMax.text = it }
                textCondition.text = city.weather.first().hourly.first().condition
                city.weather.first().hourly.first().temperature.toString()
                    .also { textTemperature.text = buildString {
                        append(it)
                        append("°")
                    } }
                img.setImageResource(getConditionIcon(textCondition.text as String))
            }

            private fun getConditionIcon(condition: String): Int {
                return when (condition.lowercase()) {
                    "sunny" -> R.drawable.sunny
                    "cloudy" -> R.drawable.cloud
                    "rainy" -> R.drawable.rainy
                    else -> R.drawable.cloudy
                }
            }
        }
}