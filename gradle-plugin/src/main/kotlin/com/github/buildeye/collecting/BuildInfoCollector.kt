package com.github.buildeye.collecting

import com.github.buildeye.collecting.property.InfoCollector
import com.github.buildeye.collecting.property.InfoProvider
import com.github.buildeye.infos.BuildInfo
import com.github.buildeye.infos.BuildResultInfo
import com.github.buildeye.infos.InfrastructureInfo
import com.github.buildeye.infos.SwitchesInfo
import com.github.buildeye.property.EitherValueFailure
import com.github.buildeye.property.Failure
import com.github.buildeye.property.Value

class BuildInfoCollector : InfoCollector<BuildInfo>("build info") {
    private val switchesInfoProvider = InfoProvider<SwitchesInfo>("switches info")
    private val infrastructureInfoProvider = InfoProvider<InfrastructureInfo>("infrastructure info")
    private val executionInfoCollector = ExecutionInfoCollector()
    private val buildResultInfoProvider = InfoProvider<BuildResultInfo>("build result info")

    override fun get(): EitherValueFailure<BuildInfo, List<String>> {
        val switchesInfo = switchesInfoProvider.get()
        val infrastructureInfo = infrastructureInfoProvider.get()
        val executionInfo = executionInfoCollector.get()
        val buildResultInfo = buildResultInfoProvider.get()

        val failures = mutableListOf<String>()
        if (switchesInfo is Failure) {
            failures.add(switchesInfo.getFailure())
        }
        if (infrastructureInfo is Failure) {
            failures.add(infrastructureInfo.getFailure())
        }
        if (executionInfo is Failure) {
            failures.addAll(executionInfo.getFailure())
        }
        if (buildResultInfo is Failure) {
            failures.add(buildResultInfo.getFailure())
        }

        return if (failures.isNotEmpty()) {
            Failure(failures)
        } else {
            Value(
                    BuildInfo(
                            (switchesInfo as Value).get(),
                            (infrastructureInfo as Value).get(),
                            (executionInfo as Value).get(),
                            (buildResultInfo as Value).get()
                    )
            )
        }
    }

    fun setSwitchesInfo(switchesInfo: SwitchesInfo) = switchesInfoProvider.set(switchesInfo)

    fun setInfrastructureInfo(infrastructureInfo: InfrastructureInfo) = infrastructureInfoProvider.set(infrastructureInfo)

    fun setBuildResultInfo(buildResultInfo: BuildResultInfo) = buildResultInfoProvider.set(buildResultInfo)

    fun getExecutionInfoCollector() = executionInfoCollector
}