package com.github.buildeye.utils

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend fun <K, V, R> Map<K, V>.parallelMapValues(transform: suspend (Map.Entry<K, V>) -> R): Map<K, R> = coroutineScope {
    mapValues { async { transform(it) } }.mapValues { it.value.await() }
}

fun <K, V> Map<K, V?>.filterValuesNotNull(): Map<K, V> {
    val destination = LinkedHashMap<K, V>()
    for (element in this) {
        element.value?.also { destination[element.key] = it }
    }
    return destination
}