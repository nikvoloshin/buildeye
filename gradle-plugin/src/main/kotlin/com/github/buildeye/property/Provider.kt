package com.github.buildeye.property

interface Provider<V, F> : SafeProperty<V, F> {
    fun set(value: V)
}