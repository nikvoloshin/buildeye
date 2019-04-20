package com.github.buildeye.listeners.taskExecution

import org.gradle.api.Task
import java.util.*
import kotlin.reflect.KProperty

typealias Listener = BuildEyeTaskExecutionListener

val Task.listeners: MutableList<Listener> by ExecutionListenersDelegate()

fun Task.beforeExecute() = this.listeners.forEach(Listener::beforeExecute)

fun Task.afterExecute() = this.listeners.forEach(Listener::afterExecute)

fun Task.addExecutionListener(listener: Listener) = this.listeners.add(listener)

class ExecutionListenersDelegate {
    private val listenersMap = IdentityHashMap<Task, MutableList<Listener>>()

    operator fun getValue(thisRef: Task, property: KProperty<*>): MutableList<Listener> {
        return listenersMap[thisRef] ?: mutableListOf<Listener>().also {
            listenersMap[thisRef] = it
        }
    }
}