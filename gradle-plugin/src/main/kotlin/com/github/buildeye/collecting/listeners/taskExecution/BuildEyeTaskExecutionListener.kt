package com.github.buildeye.collecting.listeners.taskExecution

interface BuildEyeTaskExecutionListener {
    fun beforeExecute()
    fun afterExecute()
}