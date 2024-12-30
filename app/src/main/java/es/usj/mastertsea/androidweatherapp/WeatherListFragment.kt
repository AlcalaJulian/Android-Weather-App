package es.usj.mastertsea.androidweatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.usj.mastertsea.androidweatherapp.data.WeatherAdapter
import es.usj.mastertsea.androidweatherapp.data.WeatherViewModel
import es.usj.mastertsea.androidweatherapp.domain.model.City

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WeatherListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherListFragment : Fragment() {

    private val viewModel: WeatherViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewCities)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = WeatherAdapter { city -> onCitySelected(city) }
        recyclerView.adapter = adapter

        viewModel.weatherList.observe(viewLifecycleOwner) { cities ->
            adapter.submitList(cities)
        }

        //if (viewModel.weatherList.value?.isEmpty() == true){
            viewModel.loadData(this)
        //}
    }

    private fun onCitySelected(city: City) {
        viewModel.selectData(city.city)
        // Navegar a WeatherFragment para mostrar los detalles del clima
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, WeatherDetailFragment())
            .addToBackStack(null)
            .commit()
    }
}