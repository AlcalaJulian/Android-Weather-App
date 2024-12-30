package es.usj.mastertsea.androidweatherapp.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.usj.mastertsea.androidweatherapp.R
import es.usj.mastertsea.androidweatherapp.databinding.FragmentWeatherHourlyBinding
import es.usj.mastertsea.androidweatherapp.domain.model.HourlyWeather

class HourlyWeatherAdapter(private val onClickHour: (HourlyWeather) -> Unit) : ListAdapter<HourlyWeather, HourlyWeatherAdapter.HourlyWeatherViewHolder>(HourlyWeatherDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val view = FragmentWeatherHourlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyWeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        val hourlyWeather = getItem(position)
        holder.bind(hourlyWeather)
    }

    // ViewHolder para los elementos del RecyclerView
    inner class HourlyWeatherViewHolder(private val view: FragmentWeatherHourlyBinding) : RecyclerView.ViewHolder(view.root) {
//
        fun bind(hourlyWeather: HourlyWeather) {
            view.hour.text = hourlyWeather.hour
            view.temperature.text = buildString {
                    append(hourlyWeather.temperature)
                    append("°C")
                }
            view.condition.text = hourlyWeather.condition
            view.icon.setImageResource(getConditionIcon(hourlyWeather.condition))
            itemView.setOnClickListener { onClickHour(hourlyWeather) }
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

    class HourlyWeatherDiffCallback : DiffUtil.ItemCallback<HourlyWeather>() {
        override fun areItemsTheSame(oldItem: HourlyWeather, newItem: HourlyWeather): Boolean {
            return oldItem.hour == newItem.hour
        }

        override fun areContentsTheSame(oldItem: HourlyWeather, newItem: HourlyWeather): Boolean {
            return oldItem == newItem
        }
    }
}