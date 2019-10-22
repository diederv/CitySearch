package com.deedee.citysearch.service

import java.io.RandomAccessFile

/**
 * @author diederick.
 */
class SearchInput(
    val term: String,
    val randomAccessFile: RandomAccessFile,
    val indices: MutableMap<Int, MutableMap<String, Long>>
)