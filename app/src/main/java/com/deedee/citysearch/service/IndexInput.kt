package com.deedee.citysearch.service

import java.io.BufferedReader
import java.io.RandomAccessFile

/**
 * @author diederick.
 */
class IndexInput(
    val bufferedReader: BufferedReader,
    val randomAccessFile: RandomAccessFile,
    val randomFileAlreadyExists: Boolean
)