package com.github.buildeye

import com.github.buildeye.listeners.BuildListener
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildEyePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.gradle.addBuildListener(BuildListener())
    }
}