package com.deedee.citysearch.viewmodel

import android.content.res.Resources
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.deedee.citysearch.R
import com.deedee.citysearch.service.IndexInput
import com.deedee.citysearch.service.IndexingService
import com.deedee.citysearch.util.default
import com.deedee.citysearch.util.map
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.RandomAccessFile

/**
 * @author diederick.
 */
class IndexViewModel(val resources: Resources, val randomAccessFile: RandomAccessFile, val inputResourceFile: Int): DisposingViewModel() {

    val indexReady = MutableLiveData<Boolean>().default(false)
    val progress = MutableLiveData<Int>().default(0)
    val progressAsString = progress.map { it.toString() }
    val indices = MutableLiveData<MutableMap<Int, MutableMap<String, Long>>>()

    var asyncTask: AsyncTask<IndexInput, Int, MutableMap<Int, MutableMap<String, Long>>> = IndexMaker()


    fun createIndex() {
        if (indices.value != null) {
            return
        }
        if (asyncTask?.status == AsyncTask.Status.RUNNING) {
            asyncTask.cancel(true)
        }
        asyncTask = IndexMaker()
        asyncTask.execute(
            IndexInput(
                BufferedReader(InputStreamReader(resources.openRawResource(R.raw.cities_sorted), Charsets.ISO_8859_1)),
                randomAccessFile,
                randomAccessFile.length() > 0
            )
        )
    }

    override fun onCleared() {
        asyncTask.cancel(true)
    }

    inner class IndexMaker: AsyncTask<IndexInput, Int, MutableMap<Int, MutableMap<String, Long>>>() {

        override fun doInBackground(vararg indexMakerInput: IndexInput?): MutableMap<Int, MutableMap<String, Long>> {
            return IndexingService(indexMakerInput[0]!!) { progress ->
                publishProgress(progress)
            }.createIndex()
        }

        override fun onPostExecute(result: MutableMap<Int, MutableMap<String, Long>>?) {
            super.onPostExecute(result)
            indexReady.value = true
            indices.value = result
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            progress.value = values[0]
        }

    }

}