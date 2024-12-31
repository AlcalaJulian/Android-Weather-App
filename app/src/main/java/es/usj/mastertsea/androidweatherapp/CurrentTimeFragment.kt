package es.usj.mastertsea.androidweatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.usj.mastertsea.androidweatherapp.data.WeatherCurrentAdapter
import es.usj.mastertsea.androidweatherapp.data.WeatherViewModel

class CurrentTimeFragment : Fragment() {
    private val viewModel: WeatherViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeatherCurrentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_time, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewCity)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = viewModel.selectedWeather.value?.let { WeatherCurrentAdapter(it) }!!
        recyclerView.adapter = adapter

//        viewModel.selectedWeather.observe(viewLifecycleOwner, { weather ->
//            weather?.let {
//                //textViewWeather.text = "Weather for ${weather.day}"
//                adapter.submitList(it.weather.first().hourly)
//            }
//        })
    }
}