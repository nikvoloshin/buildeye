package com.github.buildeye

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.gradle.testkit.runner.UnexpectedBuildFailure
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class BuildEyeFunctionalTest {
    @JvmField
    @TempDir
    var testProjectDir = createTempDir()

    private lateinit var buildFile: File

    @BeforeEach
    fun setUp() {
        buildFile = File("$testProjectDir/build.gradle")
    }

    @Test
    fun apply() {
        val buildFileContent = """
            buildscript {
                ext.buildeye_version = '0.1-SNAPSHOT'

                repositories {
                    mavenLocal()
                    mavenCentral()
                }

                dependencies {
                    classpath "com.github.buildeye:gradle-plugin:0.1-SNAPSHOT"
                }
            }

            apply plugin: 'com.github.buildeye'
        """.trimIndent()

        buildFile.writeText(buildFileContent)

        try {
            val result: BuildResult = GradleRunner.create()
                    .withProjectDir(testProjectDir)
                    .withArguments("build")
                    .withDebug(true)
                    .build()
            assertEquals(SUCCESS, result.task(":buildEnvironment")?.outcome)
        } catch (f: UnexpectedBuildFailure) {
            print(f.message)
        }

    }

    @Test
    fun invalidTest() {
        val buildFileContent = ""

        buildFile.writeText(buildFileContent)

        val result: BuildResult = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .build()

        assertNull(result.task(":buildEnvironment")?.outcome)

    }
}