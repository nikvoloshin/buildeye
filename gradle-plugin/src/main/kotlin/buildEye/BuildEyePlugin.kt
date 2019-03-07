package buildEye

import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildEyePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("Hello world")
    }
}