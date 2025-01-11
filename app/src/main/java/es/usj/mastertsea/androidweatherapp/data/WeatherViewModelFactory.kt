package es.usj.mastertsea.androidweatherapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.io.InputStream

class WeatherViewModelFactory(private val inputStream: InputStream) : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
//            val weathers = WeatherProvider.getJsonFromRaw(inputStream)
//            WeatherViewModel(weathers.cities) as T
//        } else {
//            throw IllegalArgumentException("Unknown ViewModel class")
//        }
//    }
}