package com.deedee.citysearch.ioc

import android.content.Context
import android.content.res.Resources
import com.deedee.citysearch.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import java.io.*
import javax.inject.Singleton

/**
 * Module which provides all the services.
 * Created by Daniel Zolnai on 2017-04-05.
 */
@Module(includes = [])
class ServiceModule {

    @Provides
    @Singleton
    internal fun getViewModelFactory(
            context: Context,
            resources: Resources,
            randomAccessFile: RandomAccessFile) =
        ViewModelFactory(
            context,
            resources,
            randomAccessFile
        )

//    @Provides
//    @Singleton
//    fun provideSearchService(context: Context, resources: Resources): SearchService {
//        val file = File(context.filesDir,"cities_sorted.json")
//        file.delete()
//        var randomAccessFileAlreadyExists = file.exists()
//        if (!randomAccessFileAlreadyExists) {
//            file.createNewFile()
//        }
//        val raf = RandomAccessFile(file, "rw")
//        return SearchService(resources, raf, randomAccessFileAlreadyExists)
//    }


//    @Provides
//    @Singleton
//    fun provideSearchService(randomAccessFile: RandomAccessFile): SearchService {
//        return SearchService(randomAccessFile)
//    }

//    @Provides
//    fun provideInputStream(resources: Resources): InputStream {
//        return resources.openRawResource(R.raw.cities_sorted)
//    }

    @Provides
    fun provideRandomAccessFile(context: Context): RandomAccessFile {
        val file = File(context.filesDir,"cities_sorted.json")
        if (!file.exists()) {
            file.createNewFile()
        }
        return RandomAccessFile(file, "rw")
    }

}

