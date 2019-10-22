package com.deedee.citysearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.deedee.citysearch.fragment.BaseFragment
import com.deedee.citysearch.viewmodel.SearchViewModel
import com.deedee.citysearch.databinding.FragmentCityMapBinding
import com.deedee.citysearch.model.City
import com.deedee.citysearch.util.Utils
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng



/**
 * @author diederick.
 */
class CityMapFragment: BaseFragment<FragmentCityMapBinding>() {

    override val layout = R.layout.fragment_city_map

    private var city: City? = null

    companion object {

        const val CITY_KEY = "city"

        fun createInstance(city: City): CityMapFragment {
            val instance = CityMapFragment()
            val args = Bundle().apply {
                putParcelable(CITY_KEY, city)
            }
            instance.arguments = args
            return instance
        }
    }

    private val searchViewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(SearchViewModel::class.java)
    }

    private val onMapReadyCallback = OnMapReadyCallback { map ->
        city?.also {
            val location = LatLng(it.coord.lat, it.coord.lon)
            map.clear()
            map?.addMarker(MarkerOptions().position(location).title(it.name))
            map?.moveCamera(CameraUpdateFactory.newLatLng(location))
            searchViewModel.loading.value = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        CitySearchApp.component().inject(this)

        binding.viewModel = searchViewModel

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        searchViewModel.selectedCity.observe(this, Observer {
            searchViewModel.loading.value = true
            city = it
            if (Utils.isLandscape(resources)) {
                mapFragment.getMapAsync(onMapReadyCallback)
            }
        })
        searchViewModel.isPortrait.value = Utils.isPortrait(resources)

        arguments?.apply {
            city = getParcelable<City>(CITY_KEY)
            mapFragment.getMapAsync(onMapReadyCallback)
        }

        return view
    }
}