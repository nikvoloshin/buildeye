package com.github.buildeye.collecting.listeners

import com.github.buildeye.collecting.Collectors
import com.github.buildeye.collecting.ExecutionData
import com.github.buildeye.infos.Change
import com.github.buildeye.infos.Change.ChangeType.*
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
            return Unknown()
        }

        val changes = determineChanges(previousSnapshot, currentSnapshot)

        return if (changes.isEmpty()) Unknown() else InputsChange(changes)
    }

    private fun determineChanges(previousSnapshot: InputsSnapshot, currentSnapshot: InputsSnapshot) = listOf(
            calcInputsChanges(previousSnapshot.properties, currentSnapshot.properties, PROPERTY),
            calcInputsChanges(previousSnapshot.inputFiles, currentSnapshot.inputFiles, FILE)
    ).flatten()

    private fun <K> calcInputsChanges(oldInputs: Map<K, *>, newInputs: Map<K, *>, inputType: Change.InputType) =
            convertDiffToInputsChanges(calcKeysDiff(oldInputs, newInputs), inputType)

    private fun <K> calcKeysDiff(oldMap: Map<K, *>, newMap: Map<K, *>) = MapsKeysDiff(
            oldMap.keys - newMap.keys,
            oldMap.keys.intersect(newMap.keys).filter { oldMap[it] != newMap[it] },
            newMap.keys - oldMap.keys
    )

    private fun convertDiffToInputsChanges(diff: MapsKeysDiff<*>, inputType: Change.InputType) = listOf(
            diff.removed.map { Change(it.toString(), REMOVED, inputType) },
            diff.changed.map { Change(it.toString(), CHANGED, inputType) },
            diff.new.map { Change(it.toString(), NEW, inputType) }
    ).flatten()

    private fun createSnapshot(inputs: TaskInputs) =
            createFilesSnapshot(inputs.files.files)?.let { InputsSnapshot(inputs.properties, it) }

    private fun createFilesSnapshot(files: Iterable<File>) = runBlocking {
        try {
            files.associateBy { it.path.toString() }.parallelMapValues { it.value.md5() }
        } catch (e: Exception) {
            return@runBlocking null
        }
    }

    private data class MapsKeysDiff<out K>(
            val removed: Iterable<K>,
            val changed: Iterable<K>,
            val new: Iterable<K>
    )
}