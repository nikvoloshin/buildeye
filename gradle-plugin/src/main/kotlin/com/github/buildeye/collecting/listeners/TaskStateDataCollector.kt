package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.Collectors
import com.github.buildeye.collecting.ExecutionData
import com.github.buildeye.infos.Change
import com.github.buildeye.infos.Change.ChangeType.CHANGED
import com.github.buildeye.infos.Change.ChangeType.NEW
import com.github.buildeye.infos.Change.InputType.FILE
import com.github.buildeye.infos.Change.InputType.PROPERTY
import com.github.buildeye.infos.InputsChange
import com.github.buildeye.infos.OutOfDateReason
import com.github.buildeye.infos.Unknown
import com.github.buildeye.inputs.InputsManager
import com.github.buildeye.inputs.snapshot.InputsSnapshot
import com.github.buildeye.utils.md5
import com.github.buildeye.utils.parallelMapValues
import kotlinx.coroutines.runBlocking
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskInputs
import org.gradle.api.tasks.TaskState
import java.io.File
import java.io.IOException

class TaskStateDataCollector(
        private val executionData: ExecutionData,
        private val inputsManager: InputsManager
) : TaskExecutionListener {
    private var inputsSnapshot: InputsSnapshot? = null

    override fun beforeExecute(task: Task) {
        inputsSnapshot = createSnapshot(task.inputs)
    }

    override fun afterExecute(task: Task, state: TaskState) {
        val outOfDateReason = if (!state.upToDate) {
            determineOutOfDateReason(inputsManager.load(task.path), inputsSnapshot)
        } else {
            null
        }

        executionData.getTaskData(task.path).stateInfo = Collectors.taskStateInfo(state, outOfDateReason)

        if (state.failure == null && inputsSnapshot != null) {
            inputsManager.save(task.path, inputsSnapshot!!)
        }
    }

    private fun determineOutOfDateReason(
            previousSnapshot: InputsSnapshot?,
            currentSnapshot: InputsSnapshot?
    ): OutOfDateReason {
        if (previousSnapshot == null || currentSnapshot == null) {
            return Unknown
        }

        val (newProperties, changedProperties) = determineChanges(previousSnapshot.properties, currentSnapshot.properties)
        val (newFiles, changedFiles) = determineChanges(previousSnapshot.inputFiles, currentSnapshot.inputFiles)

        val allChanges = newProperties.map { Change(it, NEW, PROPERTY) } +
                changedProperties.map { Change(it, CHANGED, PROPERTY) } +
                newFiles.map { Change(it, NEW, FILE) } +
                changedFiles.map { Change(it, CHANGED, FILE) }

        return if (allChanges.isEmpty()) Unknown else InputsChange(allChanges)
    }

    private fun <K, V> determineChanges(
            previousProperties: Map<K, V>,
            currentProperties: Map<K, V>
    ): Pair<List<K>, List<K>> {
        val (commonKeys, newKeys) = currentProperties.keys.partition { previousProperties.containsKey(it) }

        val newProperties = newKeys.toList()
        val changedProperties = commonKeys.filter { previousProperties[it] != currentProperties[it] }

        return Pair(newProperties, changedProperties)
    }

    private fun createSnapshot(inputs: TaskInputs) = try {
        InputsSnapshot(inputs.properties, createFilesSnapshot(inputs.files.files))
    } catch (e: IOException) {
        null
    }

    private fun createFilesSnapshot(files: Iterable<File>) = runBlocking {
        files.associateBy { it.path.toString() }.parallelMapValues { it.value.md5() }
    }
}