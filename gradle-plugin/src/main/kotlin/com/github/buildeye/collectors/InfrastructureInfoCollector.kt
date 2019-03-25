package com.github.buildeye.collectors

import com.github.buildeye.infos.InfrastructureInfo
import org.gradle.api.invocation.Gradle
import java.nio.charset.Charset
import java.util.*

class InfrastructureInfoCollector {
    fun collect(gradle: Gradle) = InfrastructureInfo(
            System.getProperty("os.name"),
            System.getProperty("os.version"),
            Runtime.getRuntime().availableProcessors(),
            gradle.startParameter.maxWorkerCount,
            System.getProperty("java.runtime.name"),
            System.getProperty("java.runtime.version"),
            System.getProperty("java.vm.name"),
            System.getProperty("java.vm.vendor"),
            System.getProperty("java.vm.version"),
            Runtime.getRuntime().maxMemory() / (1024 * 1024),
            Locale.getDefault().displayName,
            Charset.defaultCharset().displayName()
    )
}