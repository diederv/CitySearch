package com.deedee.citysearch.viewmodel

import android.os.AsyncTask
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.deedee.citysearch.model.City
import com.deedee.citysearch.service.SearchInput
import com.deedee.citysearch.service.SearchService
import com.deedee.citysearch.util.default
import java.io.RandomAccessFile

/**
 * @author diederick.
 */
class SearchViewModel(val randomAccessFile: RandomAccessFile) : DisposingViewModel() {

    // LiveData:
    val results = MutableLiveData<List<City>>()
    var indices: MutableMap<Int, MutableMap<String, Long>>? = null
    var selectedCity = MutableLiveData<City>()
    var selectedInfo = MutableLiveData<City>()
    var userAction = MutableLiveData<UserAction>()
    var loading = MutableLiveData<Boolean>().default(true)
    var noResults = MutableLiveData<Boolean>().default(false)

    val closeMap = View.OnClickListener {
        userAction.value = UserAction.CloseMap
    }

    var searchTerm: String? = ""
    val isPortrait = MutableLiveData<Boolean>()
    var asyncTask: AsyncTask<SearchInput, Void, List<City>>? = null

    override fun onCleared() {
        asyncTask?.cancel(true)
    }

    fun search(term: String) {
        indices?.also {
            if (asyncTask?.status == AsyncTask.Status.RUNNING) {
                asyncTask?.cancel(true)
            }
            asyncTask = SearchTask().apply {
                execute(
                    SearchInput(
                        term,
                        randomAccessFile,
                        it
                    )
                )
            }
        }
    }

    inner class SearchTask: AsyncTask<SearchInput, Void, List<City>>() {

        override fun doInBackground(vararg terms: SearchInput?): List<City> {
            return SearchService(terms[0]!!).search()
        }

        override fun onPostExecute(result: List<City>?) {
            super.onPostExecute(result)
            results.value = result
            noResults.value = result?.isEmpty() ?: true
        }
    }

    sealed class UserAction {
        object CloseMap: UserAction()
    }
}