package com.github.buildeye.utils

import java.io.InputStream
import java.security.DigestInputStream
import java.security.MessageDigest

fun md5(data: InputStream): ByteArray {
    val digest = MessageDigest.getInstance("MD5")
    DigestInputStream(data, digest).use {
        val buffer = ByteArray(1024)
        while (it.read(buffer, 0, 1024) > -1) {}
    }
    return digest.digest()
}

fun ByteArray.toHex(): String {
    val hexArray = "0123456789ABCDEF".toCharArray()

    val hexChars = CharArray(this.size * 2)
    for (j in this.indices) {
        val v = this[j].toInt() and 0xFF

        hexChars[j * 2] = hexArray[v ushr 4]
        hexChars[j * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
}