package com.deedee.citysearch.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author diederick.
 */
@Parcelize
data class City(

    val country: String,
    val name: String,
    @SerializedName("_id") val id: Long,
    val coord: Coordinates

): Parcelable