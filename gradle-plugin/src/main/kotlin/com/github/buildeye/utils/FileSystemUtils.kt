package com.github.buildeye.utils

import java.io.File
import java.nio.file.Files
import java.nio.file.Path


fun File.md5(): String = md5(Files.newInputStream(this.toPath())).toHex()

fun Path.removeRoot() = this.root?.relativize(this) ?: this
