package com.github.buildeye.utils

import org.gradle.internal.impldep.org.apache.commons.codec.digest.DigestUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

fun File.md5(): String = Files.newInputStream(this.toPath()).use(DigestUtils::md5Hex)

fun Path.removeRoot() = this.root?.relativize(this) ?: this
