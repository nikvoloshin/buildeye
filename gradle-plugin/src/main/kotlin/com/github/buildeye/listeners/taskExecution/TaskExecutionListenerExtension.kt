package com.github.buildeye.listeners.taskExecution

import org.gradle.api.Task
import kotlin.reflect.KProperty

typealias Listener = BuildEyeTaskExecutionListener

val Task.listeners: MutableList<Listener> by ExecutionListenersDelegate()

fun Task.beforeExecute() = this.listeners.forEach(Listener::beforeExecute)

fun Task.afterExecute() = this.listeners.forEach(Listener::afterExecute)

fun Task.addExecutionListener(listener: Listener) = this.listeners.add(listener)

class ExecutionListenersDelegate {
    private val listenersMap = mutableMapOf<String, MutableList<Listener>>()

    operator fun getValue(thisRef: Task, property: KProperty<*>): MutableList<Listener> {
        return listenersMap[thisRef.name] ?: mutableListOf<Listener>().also {
            listenersMap[thisRef.name] = it
        }
    }
}