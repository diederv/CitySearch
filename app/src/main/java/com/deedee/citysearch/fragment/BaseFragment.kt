package com.deedee.citysearch.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.deedee.citysearch.CitySearchApp

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {

    protected lateinit var binding: B

    @get:LayoutRes
    protected abstract val layout: Int

    protected val appComponent get() = CitySearchApp.component()

    protected val viewModelFactory get() = appComponent.getViewModelFactory()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<B>(inflater, layout, container, false).also {
            it.lifecycleOwner = this
        }
        return binding.root
    }
}