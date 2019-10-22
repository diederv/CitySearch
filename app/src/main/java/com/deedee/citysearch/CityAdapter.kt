package com.deedee.citysearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.ViewDataBinding
import com.deedee.citysearch.databinding.ItemCityBinding
import com.deedee.citysearch.model.City


/**
 * @author diederick.
 */
class CityAdapter(val onSelectCity: OnSelectCity, val onSelectInfo: OnSelectInfo): RecyclerView.Adapter<CityViewHolder>() {

    companion object {
        private val viewType = 1
    }

    private val cities = mutableListOf<City>()

    fun setResults(results: List<City>) {
        cities.clear()
        cities.addAll(results)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.getContext())
        val binding = ItemCityBinding.inflate(layoutInflater, parent, false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(cities.get(position), position % 2 == 0, onSelectCity, onSelectInfo)
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    interface OnSelectCity {
        fun select(city: City)
    }

    interface OnSelectInfo {
        fun select(city: City)
    }
}

class OnSelectCityImpl(val city: City, val onSelectCity: CityAdapter.OnSelectCity) {
    val onClickListener = View.OnClickListener {
        onSelectCity.select(city)
    }
}

class OnSelectInfoImpl(val city: City, val onSelectInfo: CityAdapter.OnSelectInfo) {
    val onClickListener = View.OnClickListener {
        onSelectInfo.select(city)
    }
}

class CityViewHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(city: City, even: Boolean, onSelectCity: CityAdapter.OnSelectCity, onSelectInfo: CityAdapter.OnSelectInfo) {
        binding.setVariable(BR.city, city)
        binding.setVariable(BR.cityDecorator, CityDecorator(city))
        binding.setVariable(BR.even, even)
        binding.setVariable(BR.onSelectCity, OnSelectCityImpl(city, onSelectCity))
        binding.setVariable(BR.onSelectInfo, OnSelectInfoImpl(city, onSelectInfo))
        binding.executePendingBindings()
    }
}

class CityDecorator(city: City) {
    val lat = "Lat: ${city.coord.lat}"
    val lon = "Lon: ${city.coord.lon}"
}
