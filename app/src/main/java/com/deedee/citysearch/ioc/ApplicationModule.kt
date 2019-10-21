package com.deedee.citysearch.ioc

import android.content.Context
import android.content.res.Resources
import com.deedee.citysearch.CitySearchApp

import dagger.Module
import dagger.Provides

@Module(includes = [ServiceModule::class])
class ApplicationModule(private val application: CitySearchApp) {

    @Provides
    fun provideApplicationContext(): Context {
        return application.applicationContext
    }

    @Provides
    fun provideResources(context: Context): Resources {
        return context.resources
    }
}