package com.deedee.citysearch

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.deedee.citysearch.service.IndexInput
import com.deedee.citysearch.service.IndexingService
import com.deedee.citysearch.service.SearchInput
import com.deedee.citysearch.service.SearchService

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedIndexAndSearchTest {

    val input =
        """
{"country":"BE","name":"Aalsmeer","_id":2785543,"coord":{"lon":3.96667,"lat":50.799999}}
{"country":"NL","name":"Abbekerk 1","_id":2746031,"coord":{"lon":4.75556,"lat":52.83667}}
{"country":"US","name":"Abbekerk 2","_id":4669511,"coord":{"lon":-98.03334,"lat":27.73031}}
{"country":"ES","name":"Abbekerk 3","_id":3119890,"coord":{"lon":-8.26667,"lat":42.216671}}
{"country":"ES","name":"Abcoude 1","_id":3119841,"coord":{"lon":-8.396,"lat":43.371349}}
{"country":"PT","name":"Abcoude 2","_id":8012562,"coord":{"lon":-9.32206,"lat":39.156479}}
{"country":"PT","name":"Amsterdam","_id":8012339,"coord":{"lon":-9.04547,"lat":39.314468}}
{"country":"ES","name":"Amstelveen","_id":3119746,"coord":{"lon":-8.48333,"lat":42.683331}}
"""
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    val inputStream = ByteArrayInputStream(input.toByteArray(Charsets.ISO_8859_1))
    val tempFile = File(appContext.filesDir, "raf.tmp")
    val randomAccessFile: RandomAccessFile = RandomAccessFile(tempFile, "rw")

    var indices: MutableMap<Int, MutableMap<String, Long>>? = null

    @Before
    fun setUp() {

        val indexInput = IndexInput(
            BufferedReader(InputStreamReader(inputStream, Charsets.ISO_8859_1)),
            randomAccessFile,
            false
        )
        indices = IndexingService(indexInput, {})
            .createIndex()
    }

    @Test
    fun testSearch() {

        val searchResult = SearchService(
            SearchInput(
                "abc",
                randomAccessFile,
                indices!!
            )
        ).search()

        assertEquals(searchResult.size, 2)
        assertEquals(searchResult.get(0).name, "Abcoude 1")
        assertEquals(searchResult.get(1).name, "Abcoude 2")

    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.deedee.citysearch", appContext.packageName)
    }
}
