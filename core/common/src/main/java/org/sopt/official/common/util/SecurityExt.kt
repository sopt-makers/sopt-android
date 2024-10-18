package org.sopt.official.common.util

import android.util.Base64

fun Pair<ByteArray, ByteArray>.combineToOneByteArray(): ByteArray = this.first + this.second

fun ByteArray.splitToIvAndEncryptedData(ivSize: Int): Pair<ByteArray, ByteArray> = Pair(this.copyOfRange(0, ivSize), this.copyOfRange(ivSize, this.size))

fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.DEFAULT)

fun String.toByteArray(): ByteArray = Base64.decode(this, Base64.DEFAULT)