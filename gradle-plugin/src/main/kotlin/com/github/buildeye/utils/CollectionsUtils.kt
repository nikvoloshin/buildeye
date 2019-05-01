package com.github.buildeye.utils

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend inline fun <K, V, R> Map<K, V>.parallelMapValues(
        crossinline transform: (Map.Entry<K, V>) -> R
): Map<K, R> = coroutineScope {
    mapValues { async { transform(it) } }.mapValues { it.value.await() }
}