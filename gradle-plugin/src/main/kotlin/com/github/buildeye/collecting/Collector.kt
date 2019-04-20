package com.github.buildeye.collecting

interface Collector<out T> {
    fun collect(): T
}