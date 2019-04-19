package com.github.buildeye.listeners.taskExecution

interface BuildEyeTaskExecutionListener {
    fun beforeExecute()
    fun afterExecute()
}