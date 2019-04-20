package com.github.buildeye.utils

import com.github.buildeye.infos.FailureInfo
import java.util.concurrent.TimeUnit

fun millisTime() = TimeUnit.NANOSECONDS.toMillis(System.nanoTime())

fun createFailureInfo(failure: Throwable) = FailureInfo(
        failure.message ?: "",
        failure.cause?.toString() ?: ""
)

fun createFailureInfoOfNullable(failure: Throwable?) = when (failure) {
    null -> null
    else -> createFailureInfo(failure)
}