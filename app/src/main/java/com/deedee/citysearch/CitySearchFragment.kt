package com.deedee.citysearch

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.deedee.citysearch.databinding.FragmentCitySearchBinding
import com.deedee.citysearch.fragment.BaseFragment
import com.deedee.citysearch.viewmodel.SearchViewModel
import android.text.Editable
import com.deedee.citysearch.model.City
import com.deedee.citysearch.util.setupClearButtonWithAction
import java.util.*


/**
 * @author diederick.
 */
class CitySearchFragment: BaseFragment<FragmentCitySearchBinding>() {

    override val layout = R.layout.fragment_city_search

    private val searchViewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(SearchViewModel::class.java)
    }

    val onSelectCity = object: CityAdapter.OnSelectCity {
        override fun select(city: City) {
            searchViewModel.selectedCity.value = city
        }
    }

    val onSelectInfo = object: CityAdapter.OnSelectInfo {
        override fun select(city: City) {
            searchViewModel.selectedInfo.value = city
        }
    }

    val adapter = CityAdapter(onSelectCity, onSelectInfo)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        CitySearchApp.component().inject(this)
        binding.viewModel = searchViewModel
        binding.filter.addTextChangedListener(textWatcher)

        binding.result.adapter = adapter
        binding.filter.setupClearButtonWithAction()

        searchViewModel.results.observe(this, androidx.lifecycle.Observer {
            adapter.setResults(it)
        })

        return view
    }

    val textWatcher = object: TextWatcher {

        private var timer: Timer? = null

        override fun afterTextChanged(editable: Editable) {
            searchViewModel.searchTerm = editable.toString()
            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    searchViewModel.search(editable.toString())
                }
            }, 600)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            timer?.cancel()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.filter.setText(searchViewModel.searchTerm)
    }

}