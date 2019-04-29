package com.github.buildeye.utils

import org.gradle.internal.impldep.org.apache.commons.codec.digest.DigestUtils
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

fun File.md5(): String? {
    return try {
        Files.newInputStream(this.toPath()).use(DigestUtils::md5Hex)
    } catch (e: IOException) {
        null
    }
}

fun Path.removeRoot() = this.root?.relativize(this) ?: this
