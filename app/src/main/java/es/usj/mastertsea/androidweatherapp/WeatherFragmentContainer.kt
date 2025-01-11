package es.usj.mastertsea.androidweatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import es.usj.mastertsea.androidweatherapp.data.WeatherViewModel

class WeatherFragmentContainer : Fragment() {

    //private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel.loadData(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragmenty
        return inflater.inflate(R.layout.fragment_weather_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {

            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentWeatherContainerView, WeatherListFragment())
                .commit()
            //viewModel.loadData(this)
        }
    }
}