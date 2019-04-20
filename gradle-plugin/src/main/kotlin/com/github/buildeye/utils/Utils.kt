package com.github.buildeye.utils

import java.util.concurrent.TimeUnit

fun millisTime() = TimeUnit.NANOSECONDS.toMillis(System.nanoTime())