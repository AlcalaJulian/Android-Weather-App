package es.usj.mastertsea.androidweatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.usj.mastertsea.androidweatherapp.data.WeatherAdapter
import es.usj.mastertsea.androidweatherapp.data.WeatherViewModel
import es.usj.mastertsea.androidweatherapp.domain.model.City

class WeatherListFragment : Fragment() {

    private val viewModel: WeatherViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeatherAdapter
    private lateinit var editTextSearch: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextSearch = view.findViewById(R.id.editTextSearch)
        recyclerView = view.findViewById(R.id.recyclerViewCities)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = WeatherAdapter { city -> onCitySelected(city) }
        recyclerView.adapter = adapter

        viewModel.weatherList.observe(viewLifecycleOwner) { cities ->
            adapter.submitList(cities)
        }

        viewModel.loadData(this)

        editTextSearch.addTextChangedListener{ text ->
            adapter.filter.filter(text)
        }
    }

    private fun onCitySelected(city: City) {
        viewModel.selectData(city.city)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, WeatherDetailFragment())
            .addToBackStack(null)
            .commit()
    }
}