package com.deedee.citysearch.viewmodel

import android.content.res.Resources
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.deedee.citysearch.R
import com.deedee.citysearch.model.City
import com.deedee.citysearch.util.crop
import com.deedee.citysearch.util.default
import com.deedee.citysearch.util.map
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.RandomAccessFile

/**
 * @author diederick.
 */
class IndexViewModel(val resources: Resources, val randomAccessFile: RandomAccessFile): DisposingViewModel() {

    companion object {
        val INDEX_HEAD_SIZE: Int = 4
    }

    val indexReady = MutableLiveData<Boolean>().default(false)
    val progress = MutableLiveData<Int>().default(0)
    val progressAsString = progress.map { it.toString() }
    val indices = MutableLiveData<MutableMap<Int, MutableMap<String, Long>>>()

    var asyncTask: AsyncTask<IndexMakerInput, Int, MutableMap<Int, MutableMap<String, Long>>> = IndexMaker()


    fun createIndex() {
        if (indices.value != null) {
            return
        }
        if (asyncTask?.status == AsyncTask.Status.RUNNING) {
            asyncTask.cancel(true)
        }
        asyncTask = IndexMaker()
        asyncTask.execute(
            IndexMakerInput(
                BufferedReader(InputStreamReader(resources.openRawResource(R.raw.cities_sorted), Charsets.ISO_8859_1)),
                randomAccessFile,
                randomAccessFile.length() > 0
            )
        )
    }


    override fun onCleared() {
        asyncTask.cancel(true)
    }

    inner class IndexMakerInput(
        val bufferedReader: BufferedReader,
        val randomAccessFile: RandomAccessFile,
        val randomFileAlreadyExists: Boolean
    )

    inner class IndexMaker: AsyncTask<IndexMakerInput, Int, MutableMap<Int, MutableMap<String, Long>>>() {


        private var gson = Gson()
        var previousHead: String = ""
        private val localIndices = mutableMapOf<Int, MutableMap<String, Long>>()

        override fun doInBackground(vararg indexMakerInput: IndexMakerInput?): MutableMap<Int, MutableMap<String, Long>> {

            var count = 0
            var i = 0L

            val input: IndexMakerInput = indexMakerInput[0]!!

            input.bufferedReader.forEachLine { line ->
                val s = String(line.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
                val line_with_newline = s + "\n"
                if (!input.randomFileAlreadyExists) {
                    input.randomAccessFile.write(line_with_newline.toByteArray(Charsets.ISO_8859_1))
                }
                processCity(s, i)
                i += line_with_newline.toByteArray(Charsets.ISO_8859_1).size
                count++
                if (count % 1000 == 0) {
                    publishProgress(100 - (((209556 - count).toDouble() / 209556.toDouble()) * 100).toInt())
                }
            }
            input.bufferedReader.close()
            Log.e("SearchService", "Index created!")
            return localIndices
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


        private fun processCity(line: String, pos: Long) {
            val city = parse(line.trim())
            val headSize = INDEX_HEAD_SIZE
            for (i in headSize downTo 1) {
                var head = city.name.crop(i)
                val map = localIndices.getOrPut(i) { mutableMapOf<String, Long>() }
                if (head.length > i-1 && (head != previousHead.crop(i))) {
                    map.put(head.toLowerCase(), pos)
                }
            }
            previousHead = city.name
        }

        private fun parse(json: String): City {
            try {
                return gson.fromJson<City>(json, City::class.java)
            } catch(e: Exception) {
                Log.e("SearchService: Exc:", json)
                throw e
            }
        }

    }

}