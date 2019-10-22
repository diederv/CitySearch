package com.deedee.citysearch.service

import android.util.Log
import com.deedee.citysearch.model.City
import com.deedee.citysearch.util.crop
import com.google.gson.Gson

/**
 * @author diederick.
 */
class IndexingService(val input: IndexInput, val publishProgress: (Int) -> Unit) {

    companion object {
        val INDEX_HEAD_SIZE: Int = 4
    }

    private var gson = Gson()
    var previousHead: String = ""
    private val localIndices = mutableMapOf<Int, MutableMap<String, Long>>()

    fun createIndex(): MutableMap<Int, MutableMap<String, Long>> {

        var count = 0
        var i = 0L

        /*
        This service creates (n) indices (n = 4 (INDEX_HEAD_SIZE) in this configuration)
        the first index is a map of all occuring first single characters, with a pointer to the first occurence of a city-match in a sorted random-access-file
        the second index is a map of all occuring combinations of the first two characters. Etc.

        Imagine the following test-cities (alphabetically sorted by name):

        0:Aalsmeer
        1:Abbekerk 1
        2:Abbekerk 2
        3:Abbekerk 3
        4:Abcoude 1
        5:Abcoude 2
        6:Amsterdam
        7:Amstelveen

        index 1:
        a -> pointer to line: 0

        index 2:
        aa -> pointer to line: 0
        ab -> pointer to line: 1
        am -> pointer to line: 6

        index 3:
        aal -> pointer to line: 0
        abb -> pointer to line: 1
        abc -> pointer to line: 4
        ams -> pointer to line: 6

        when a call is made to the SearchService, a look-up is done using the search term, on the biggest relevant index.
        The returned value points to the first occurence (if any) of one ore more occurences of cities that have a name that starts with that term.
        The following occurences will be read until the first occurence that doesn't match the term.

         */

        input.bufferedReader.forEachLine { line ->
            val s = String(line.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
            if (!s.isEmpty()) {
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
        }
        input.bufferedReader.close()
        Log.e("SearchService", "Index created!")
        return localIndices

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
        return gson.fromJson<City>(json, City::class.java)
    }
}

