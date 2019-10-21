package com.deedee.citysearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.deedee.citysearch.databinding.ActivityMainBinding
import com.deedee.citysearch.viewmodel.IndexViewModel
import com.deedee.citysearch.viewmodel.SearchViewModel
import com.deedee.citysearch.viewmodel.ViewModelFactory
import javax.inject.Inject
import androidx.fragment.app.Fragment
import com.deedee.citysearch.model.City
import com.deedee.citysearch.util.Utils
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {

    protected lateinit var binding: ActivityMainBinding

    @get:LayoutRes
    protected val layout: Int = R.layout.activity_main

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val searchViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    private val indexViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(IndexViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CitySearchApp.component().inject(this)

        binding = DataBindingUtil.setContentView(this, layout)
        binding.lifecycleOwner = this
        binding.viewModel = indexViewModel

        indexViewModel.indices.observe(this, Observer {
            searchViewModel.indices = it
        })

        searchViewModel.selectedCity.observe(this, Observer { city ->
            if (Utils.isPortrait(resources)) {
                replaceFragment(CityMapFragment.createInstance(city), true)
            } else {

            }
        })
        searchViewModel.selectedInfo.observe(this, Observer { city ->
            showCityInfo(city)
        })
        searchViewModel.userAction.observe(this, Observer { action ->
            when (action) {
                is SearchViewModel.UserAction.CloseMap -> replaceFragment(CitySearchFragment(), true)
            }
        })
        replaceFragment(CitySearchFragment(), true)
        if (Utils.isLandscape(resources)) {
            replaceFragment(CityMapFragment(), false)
        }
        indexViewModel.createIndex()
    }

    private fun replaceFragment(newFragment: Fragment, first: Boolean) {
        val containerViewId = if (first) R.id.fragment_container else R.id.fragment_2nd_container
        supportFragmentManager
            .beginTransaction()
            .replace(containerViewId, newFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showCityInfo(city: City) {
        AlertDialog.Builder(this)

            .setTitle("City: ${city.name} (${city.country})")
            .setMessage("Coordinates: \nLatitude: ${city.coord.lat} \nLongitude: ${city.coord.lon}")

            .setPositiveButton(R.string.ok,
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })

            .setIcon(R.drawable.ic_location_city)
            .show()
    }
}
