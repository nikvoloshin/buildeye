package com.github.buildeye.time

import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class Stopwatch {
    private var isRunning = false
    private var startingPoint: Long by Delegates.notNull()
    private var finishingPoint: Long by Delegates.notNull()

    fun start() {
        check(!isRunning) { "Attempt to start measuring when it's already running" }
        isRunning = true
        startingPoint = System.nanoTime()
    }

    fun split(): Long {
        check(isRunning) { "Attempt to get split time when it's not running" }
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startingPoint)
    }

    fun stop() {
        check(isRunning) { "Attempt to stop measuring when it's not running" }
        isRunning = false
        finishingPoint = System.nanoTime()
    }

    fun elapsed(): Long {
        check(!isRunning) { "Attempt to get elapsed time when it's running" }
        return TimeUnit.NANOSECONDS.toMillis(finishingPoint - startingPoint)
    }
}