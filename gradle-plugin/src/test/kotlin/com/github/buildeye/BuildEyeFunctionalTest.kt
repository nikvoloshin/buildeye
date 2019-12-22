package com.github.buildeye

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.rules.TemporaryFolder
import java.io.File


internal class BuildEyeFunctionalTest {
    @Rule
    val testProjectDir: TemporaryFolder = TemporaryFolder()

    private lateinit var settingsFile: File
    private lateinit var buildFile: File

    @BeforeEach
    fun setUp() {
        settingsFile = testProjectDir.newFile("settings.gradle");
        buildFile = testProjectDir.newFile("build.gradle");
    }

    @Test
    fun test() {
        val buildeye_version = "0.1-SNAPSHOT"
        val buildFileContent = """
            buildscript {
                ext.buildeye_version = '0.1-SNAPSHOT'

                repositories {
                    mavenLocal()
                    mavenCentral()
                }

                dependencies {
                    classpath "com.github.buildeye:gradle-plugin:$buildeye_version"
                }
            }

            apply plugin: 'com.github.buildeye'
        """.trimIndent()

        buildFile.writeText(buildFileContent)

        val result: BuildResult = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments("test")
                .build()

        print(result.output)
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun apply() {
    }
}