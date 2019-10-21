package com.deedee.citysearch

import androidx.multidex.MultiDexApplication
import com.deedee.citysearch.ioc.AppComponent
import com.deedee.citysearch.ioc.ApplicationModule
import com.deedee.citysearch.ioc.DaggerAppComponent

/**
 * @author diederick.
 */
class CitySearchApp: MultiDexApplication() {

    companion object {
        private lateinit var component: AppComponent
        fun component(): AppComponent {
            return component
        }
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
            .also {
                it.inject(this)
            }
    }

}