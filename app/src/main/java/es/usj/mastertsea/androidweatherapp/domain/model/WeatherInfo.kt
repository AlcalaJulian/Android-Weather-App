package es.usj.mastertsea.androidweatherapp.domain.model

import com.google.gson.annotations.SerializedName
import com.google.gson.Gson

data class WeatherInfo(
    @SerializedName("cities") val cities: List<City>
)

data class City(
    @SerializedName("city") val city: String,
    @SerializedName("location") val location: Location,
    @SerializedName("weather") val weather: List<WeatherDay>
)

data class Location(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)

data class WeatherDay(
    @SerializedName("day") val day: String,
    @SerializedName("hourly") val hourly: List<HourlyWeather>
)

data class HourlyWeather(
    @SerializedName("hour") val hour: String,
    @SerializedName("temperature") val temperature: Int,
    @SerializedName("condition") val condition: String,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("wind_speed") val windSpeed: Int
)

fun parseWeather(jsonString: String): WeatherInfo {
    return Gson().fromJson(jsonString, WeatherInfo::class.java)
}