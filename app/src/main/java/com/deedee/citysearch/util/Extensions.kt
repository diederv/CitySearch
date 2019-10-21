package com.deedee.citysearch.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

/**
 * @author diederick.
 */
inline fun <reified T : Any> classOf() = T::class.java

fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

fun String.crop(maxLength: Int): String = this.substring(0, Math.min(maxLength, this.length))

/**
 * Extension to perform a [Transformations.map] wrapped in a lambda.
 */
fun <T, R> LiveData<T>.map(func: (T) -> R): LiveData<R> = Transformations.map(this, func)