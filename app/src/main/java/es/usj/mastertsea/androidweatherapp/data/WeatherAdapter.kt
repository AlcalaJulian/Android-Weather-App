    package es.usj.mastertsea.androidweatherapp.data

    import android.view.LayoutInflater
    import android.view.ViewGroup
    import android.widget.Filter
    import android.widget.Filterable
    import androidx.recyclerview.widget.DiffUtil
    import androidx.recyclerview.widget.ListAdapter
    import androidx.recyclerview.widget.RecyclerView
    import es.usj.mastertsea.androidweatherapp.databinding.WeatherItemBinding
    import es.usj.mastertsea.androidweatherapp.domain.model.City
    import es.usj.mastertsea.androidweatherapp.domain.util.WeatherUtil.Companion.getCurrentHour
    import es.usj.mastertsea.androidweatherapp.domain.util.WeatherUtil.Companion.getCurrentWeather
    import java.text.SimpleDateFormat
    import java.util.Date
    import java.util.Locale

    class WeatherAdapter (private val onCityClick: (City) -> Unit) :
        ListAdapter<City, WeatherAdapter.WeatherViewHolder>(CityDiffCallback()), Filterable {
        private var filteredList: List<City> = currentList

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
                val binding: WeatherItemBinding = WeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return WeatherViewHolder(binding)
            }

            override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
                    val city = getItem(position)
                    holder.bind(city)
            }

            inner class WeatherViewHolder(private val view: WeatherItemBinding) : RecyclerView.ViewHolder(view.root) {

                fun bind(city: City) {
                    view.textItemCity.text = city.city

                    "Max: ${city.weather.first().hourly.maxOf { it.temperature }}° Min: ${city.weather.first().hourly.maxOf { it.temperature }}°".also { view.textItemMaxMin.text = it }

                    val now = Date()
                    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Define the format
                    val formattedDate = format.format(now) // Convert to string

                    val currentWeather = getCurrentWeather(city.weather, formattedDate)
                    val currentHour = getCurrentHour(currentWeather.hourly, formattedDate)

                    currentHour.temperature.toString().also { view.textItemHour.text = it }
                    view.textItemCondition.text = currentHour.condition
                    view.textItemHour.text = currentHour.hour
                    currentHour.temperature.toString()
                        .also { view.textItemTemperature.text = buildString {
                                    append(it)
                                    append("°")
                                }
                            }

                    itemView.setOnClickListener {
                        onCityClick(city)
                    }
                }
            }

            class CityDiffCallback : DiffUtil.ItemCallback<City>() {
                override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
                    return oldItem.city == newItem.city
                }

                override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
                    return oldItem == newItem
                }
            }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    if (filteredList.isEmpty()){
                        filteredList = currentList
                    }

                    val filterResults = FilterResults()
                    filterResults.values = if (constraint.isNullOrEmpty()) {
                        filteredList  // Usamos currentList directamente
                    } else {
                        filteredList.filter { it.city.contains(constraint, ignoreCase = true) }
                    }

                    return filterResults
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    submitList(results?.values as List<City>)  // Usamos submitList para actualizar la lista filtrada
                }
            }
        }
    }