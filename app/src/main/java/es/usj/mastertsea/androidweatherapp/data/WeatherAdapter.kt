    package es.usj.mastertsea.androidweatherapp.data

    import android.view.LayoutInflater
    import android.view.ViewGroup
    import androidx.recyclerview.widget.DiffUtil
    import androidx.recyclerview.widget.ListAdapter
    import androidx.recyclerview.widget.RecyclerView
    import es.usj.mastertsea.androidweatherapp.databinding.WeatherItemBinding
    import es.usj.mastertsea.androidweatherapp.domain.model.City

    class WeatherAdapter (private val onCityClick: (City) -> Unit) :
        ListAdapter<City, WeatherAdapter.WeatherViewHolder>(CityDiffCallback()) {

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
                    city.weather.first().hourly.first().temperature.toString().also { view.textItemHour.text = it }
                    view.textItemCondition.text = city.weather.first().hourly.first().condition
                    view.textItemHour.text = city.weather.first().hourly.first().hour
                    city.weather.first().hourly.first().temperature.toString()
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
    }