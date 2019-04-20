package com.github.buildeye.utils

import com.github.buildeye.infos.FailureInfo
import org.gradle.internal.impldep.org.apache.commons.lang.exception.ExceptionUtils
import java.util.concurrent.TimeUnit

fun millisTime() = TimeUnit.NANOSECONDS.toMillis(System.nanoTime())

fun createFailureInfo(failure: Throwable) = FailureInfo(
        failure.message ?: "",
        ExceptionUtils.getFullStackTrace(failure)
)

fun createFailureInfoOfNullable(failure: Throwable?) = when (failure) {
    null -> null
    else -> createFailureInfo(failure)
}