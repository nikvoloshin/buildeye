package com.github.buildeye.time

import java.util.concurrent.TimeUnit

class SplitStopwatch {
    private var startingPoint: Long = -1L

    fun start() {
        check(!isRunning()) { "Attempt to start measuring when it's already started" }
        startingPoint = System.nanoTime()
    }

    fun split(): Long {
        check(isRunning()) { "Attempt to get elapsed time before measuring started" }
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startingPoint)
    }

    private fun isRunning() = startingPoint != -1L
}