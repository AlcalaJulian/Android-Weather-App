package es.usj.mastertsea.androidweatherapp.domain.model

import com.google.gson.annotations.SerializedName

data class WeatherInfo(
    @SerializedName("cities") var cities: List<City>
)

data class City(
    @SerializedName("city") var city: String,
    @SerializedName("location") var location: Location,
    @SerializedName("weather") var weather: List<WeatherDay>
)

data class Location(
    @SerializedName("latitude") var latitude: Double,
    @SerializedName("longitude") var longitude: Double
)

data class WeatherDay(
    @SerializedName("day") var day: String,
    @SerializedName("hourly") var hourly: List<HourlyWeather>
){

    var max: Int? = hourly.maxOfOrNull { it.temperature }
    var min: Int? = hourly.minOfOrNull { it.temperature }
}

data class HourlyWeather(
    @SerializedName("hour") var hour: String,
    @SerializedName("temperature") var temperature: Int,
    @SerializedName("condition") var condition: String,
    @SerializedName("humidity") var humidity: Int,
    @SerializedName("wind_speed") var windSpeed: Int
)