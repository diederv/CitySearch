package com.deedee.citysearch.ioc

import android.content.Context
import android.content.res.Resources
import com.deedee.citysearch.R
import com.deedee.citysearch.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import java.io.*
import javax.inject.Singleton

/**
 * @author diederick.
 */
@Module(includes = [])
class ServiceModule {

    companion object {
        private val inputResourceFile: Int = R.raw.cities_sorted
        private val randomAccessFileName: String = "cities_sorted.json"
    }

    @Provides
    @Singleton
    fun getViewModelFactory(
            context: Context,
            resources: Resources,
            randomAccessFile: RandomAccessFile) =
        ViewModelFactory(
            context,
            resources,
            inputResourceFile,
            randomAccessFile
        )

    @Provides
    fun provideRandomAccessFile(context: Context): RandomAccessFile {
        val file = File(context.filesDir,randomAccessFileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        return RandomAccessFile(file, "rw")
    }
}

