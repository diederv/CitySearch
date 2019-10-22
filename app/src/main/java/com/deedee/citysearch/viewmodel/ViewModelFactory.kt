package com.deedee.citysearch.viewmodel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.deedee.citysearch.util.classOf
import java.io.RandomAccessFile

/**
 * @author diederick.
 */
class ViewModelFactory(
    private val resources: Resources,
    private val inputResourceFile: Int,
    private val randomAccessFile: RandomAccessFile
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(classOf<SearchViewModel>()) -> SearchViewModel(randomAccessFile) as T
            modelClass.isAssignableFrom(classOf<IndexViewModel>()) -> IndexViewModel(resources, randomAccessFile, inputResourceFile) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}