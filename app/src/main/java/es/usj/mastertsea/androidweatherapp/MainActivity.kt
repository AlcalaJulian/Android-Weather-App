package es.usj.mastertsea.androidweatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.usj.mastertsea.androidweatherapp.databinding.ActivityMainBinding
import es.usj.mastertsea.androidweatherapp.domain.model.parseWeather

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initJson()
    }

    private fun initJson() {
        val inputStream = resources.openRawResource(R.raw.forecast_scheme)
        val json = inputStream.bufferedReader().use { it.readText() }
        val weatherList = parseWeather(jsonString = json)
        Log.i("JSON WEATHER", weatherList.toString())
    }
}
