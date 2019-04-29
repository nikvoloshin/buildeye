package com.github.buildeye.inputs

import com.github.buildeye.inputs.snapshot.InputsSnapshot
import com.github.buildeye.utils.deserialize
import com.github.buildeye.utils.removeRoot
import com.github.buildeye.utils.serialize
import org.gradle.api.Project
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths

class InputsManager(project: Project) {
    private val relativeDir = Paths.get("buildeye/snapshots")
    private val snapshotsDir = project.buildDir.toPath().resolve(relativeDir)

    fun save(taskPath: String, snapshot: InputsSnapshot) = serialize(getTaskSnapshotsPath(taskPath), snapshot)

    fun load(taskPath: String) = try {
        deserialize<InputsSnapshot>(getTaskSnapshotsPath(taskPath))
    } catch (e: FileNotFoundException) {
        null
    }


    private fun getTaskSnapshotsPath(taskPath: String): Path {
        val systemPath = Paths.get(taskPath.replace(":", File.separator)).removeRoot()
        return snapshotsDir.resolve(systemPath)
    }
}