package com.github.buildeye.utils

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend fun <T, R> Iterable<T>.parallelMap(transform: suspend (T) -> R): List<R> = coroutineScope {
    map { async { transform(it) } }.map { it.await() }
}
