package com.deedee.citysearch.ioc

import com.deedee.citysearch.*
import com.deedee.citysearch.viewmodel.ViewModelFactory
import dagger.Component
import javax.inject.Singleton

/**
 * @author diederick.
 */
@Singleton
@Component(modules = [ApplicationModule::class])
interface AppComponent {

    fun inject(app: CitySearchApp)

    fun inject(activity: MainActivity)

    fun inject(fragment: CitySearchFragment)
    fun inject(fragment: CityMapFragment)

    // Provisioning
    fun getViewModelFactory(): ViewModelFactory

}