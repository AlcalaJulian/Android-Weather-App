package es.usj.mastertsea.androidweatherapp.data

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import es.usj.mastertsea.androidweatherapp.R
import es.usj.mastertsea.androidweatherapp.domain.model.City
import es.usj.mastertsea.androidweatherapp.domain.model.WeatherInfo
import java.io.InputStreamReader

class WeatherViewModel: ViewModel() {
    private var weatherInfoList = MutableLiveData<List<City>>()
    val weatherList: LiveData<List<City>> get() = weatherInfoList

    private val _selectedCityWeather = MutableLiveData<City>()
    val selectedWeather: LiveData<City> get() = _selectedCityWeather

    fun selectData(id: String){
        _selectedCityWeather.value = weatherInfoList.value?.first { it -> it.city == id }
    }

    fun getImg(){
        //selectedWeather.weather.first().
    }

    fun loadData(context: Fragment){
        try {
            val jsonString = readJsonFromRaw(context, R.raw.forecast_scheme)
            val weather = Gson().fromJson(jsonString, WeatherInfo::class.java)//Json.decodeFromString<WeatherInfo>(jsonString)
            weatherInfoList.postValue(weather.cities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private  fun readJsonFromRaw(context: Fragment, weather: Int): String {
        val inputStream = context.resources.openRawResource(weather)
        val reader = InputStreamReader(inputStream)
        return reader.readText()
    }
}