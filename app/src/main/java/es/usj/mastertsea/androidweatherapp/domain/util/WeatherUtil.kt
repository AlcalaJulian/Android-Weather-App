package es.usj.mastertsea.androidweatherapp.domain.util

import es.usj.mastertsea.androidweatherapp.domain.model.HourlyWeather
import es.usj.mastertsea.androidweatherapp.domain.model.WeatherDay

class WeatherUtil {
    companion object{

        fun getCurrentWeather(weather: List<WeatherDay>, dateString: String): WeatherDay {

            return weather.firstOrNull() { dateString.contains(it.day) } ?: weather.first()
            // .hourly.first { formattedDate.contains(it.hour) }
        }

        fun getCurrentHour(hours: List<HourlyWeather>, dateString: String): HourlyWeather {
            return hours.firstOrNull { dateString.contains(it.hour) } ?: hours.first()
        }
    }
}