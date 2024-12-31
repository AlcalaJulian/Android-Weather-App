package es.usj.mastertsea.androidweatherapp.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.usj.mastertsea.androidweatherapp.R
import es.usj.mastertsea.androidweatherapp.databinding.WeatherDayBinding
import es.usj.mastertsea.androidweatherapp.domain.model.WeatherDay

class DayWeatherAdapter(private val onClickHour: (WeatherDay) -> Unit) : ListAdapter<WeatherDay, DayWeatherAdapter.DayWeatherViewHolder>(DayWeatherDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayWeatherViewHolder {
        val view = WeatherDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayWeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayWeatherViewHolder, position: Int) {
        val dayWeather = getItem(position)
        holder.bind(dayWeather)
    }

    inner class DayWeatherViewHolder(private val view: WeatherDayBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind(dayWeather: WeatherDay) {
            view.textDay.text = dayWeather.day
            dayWeather.hourly.first().temperature.toString().also { view.textTemperatureStart.text = it }
            dayWeather.hourly.last().temperature.toString().also { view.textTemperatureEnd.text = it }

    view.icon.setImageResource(getConditionIcon(dayWeather.hourly.first().condition))
            itemView.setOnClickListener { onClickHour(dayWeather) }
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

    class DayWeatherDiffCallback : DiffUtil.ItemCallback<WeatherDay>() {
        override fun areItemsTheSame(oldItem: WeatherDay, newItem: WeatherDay): Boolean {
            return oldItem.day == newItem.day
        }

        override fun areContentsTheSame(oldItem: WeatherDay, newItem: WeatherDay): Boolean {
            return oldItem == newItem
        }
    }
}