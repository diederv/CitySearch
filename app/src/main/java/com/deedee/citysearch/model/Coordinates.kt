package com.deedee.citysearch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author diederick.
 */
@Parcelize
data class Coordinates(

    val lon: Double,
    val lat: Double

): Parcelable