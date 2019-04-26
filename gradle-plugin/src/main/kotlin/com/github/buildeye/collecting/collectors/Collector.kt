package com.github.buildeye.collecting.collectors

interface Collector<out T> {
    fun collect(): T
}