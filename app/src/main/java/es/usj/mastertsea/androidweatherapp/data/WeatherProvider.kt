package es.usj.mastertsea.androidweatherapp.data

import android.util.Log
import es.usj.mastertsea.androidweatherapp.domain.model.WeatherInfo
import kotlinx.serialization.json.Json
import java.io.InputStream

class WeatherProvider {

    companion object{

        fun getJsonFromRaw(jsonStream: InputStream): WeatherInfo {
            val json = jsonStream.bufferedReader().use { it.readText() }
            val weather = parseStringToWeather(jsonString = json)

            Log.i("JSON WEATHER", weather.toString())
            return weather
        }

        private fun parseStringToWeather(jsonString: String): WeatherInfo{
            return Json.decodeFromString<WeatherInfo>(jsonString)
        }
    }
}