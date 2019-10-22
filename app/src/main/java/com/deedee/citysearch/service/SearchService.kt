package com.deedee.citysearch.service

import android.util.Log
import com.deedee.citysearch.model.City
import com.deedee.citysearch.util.crop
import com.google.gson.Gson

/**
 * @author diederick.
 */
class SearchService(val input: SearchInput) {

    private var gson = Gson()

    fun search(): List<City> {

        /*

        See the class: IndexingService for an exmplaination on the mechanics of this search-service

        */

        val prefix = input.term.toLowerCase()
        val result = mutableListOf<City>()
        if (input.term.isEmpty()) {
            return result
        }
        var pos = 0L
        var maxHeadSize = 0
        val headSize = Math.min(prefix.length, IndexingService.INDEX_HEAD_SIZE)
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
}