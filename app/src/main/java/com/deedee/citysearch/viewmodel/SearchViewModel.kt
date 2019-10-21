package com.deedee.citysearch.viewmodel

import android.content.res.Resources
import android.os.AsyncTask
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.deedee.citysearch.model.City
import com.deedee.citysearch.util.Utils
import com.deedee.citysearch.util.crop
import com.deedee.citysearch.util.default
import com.google.gson.Gson
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

        private var gson = Gson()

        override fun doInBackground(vararg terms: SearchInput?): List<City> {
            return search(terms[0]!!)
        }

        private fun search(input: SearchInput): List<City> {
            val prefix = input.term.toLowerCase()
            val result = mutableListOf<City>()
            if (input.term.isEmpty()) {
                return result
            }
            var pos = 0L
            var maxHeadSize = 0
            val headSize = IndexViewModel.INDEX_HEAD_SIZE
            loop@ for (i in headSize downTo 1) {
                if (input.indices.get(i)?.containsKey(prefix.crop(i)) ?: false) {
                    pos = input.indices.get(i)?.get(prefix.crop(i)) ?: 0L
                    maxHeadSize = i
                    break@loop
                }
            }
            input.randomAccessFile.seek(pos)
            var match = true
            while (match) {
                val line = input.randomAccessFile.readLine()
                val s = String(line.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
                val city = parse(s)
                match = city.name.toLowerCase().startsWith(prefix.crop(maxHeadSize))
                if (city.name.toLowerCase().startsWith(prefix)) {
                    result.add(city)
                }
            }
            return result
        }

        private fun parse(json: String): City {
            try {
                return gson.fromJson<City>(json, City::class.java)
            } catch(e: Exception) {
                Log.e("SearchService: Exc:", json)
                throw e
            }
        }

        override fun onPostExecute(result: List<City>?) {
            super.onPostExecute(result)
            results.value = result
            noResults.value = result?.isEmpty() ?: true
        }

    }

    inner class SearchInput(
        val term: String,
        val randomAccessFile: RandomAccessFile,
        val indices: MutableMap<Int, MutableMap<String, Long>>
    )

    sealed class UserAction {
        object CloseMap: UserAction()
    }


}