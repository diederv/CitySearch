package com.deedee.citysearch.viewmodel

import android.os.AsyncTask
import androidx.lifecycle.ViewModel

/**
 * @author diederick.
 */
abstract class DisposingViewModel : ViewModel() {

    abstract override fun onCleared()
}