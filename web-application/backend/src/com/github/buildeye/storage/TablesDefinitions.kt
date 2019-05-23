package com.github.buildeye.storage

import com.github.buildeye.infos.BuildResultInfo
import com.github.buildeye.infos.Change
import com.github.buildeye.infos.OutOfDateReason
import org.jetbrains.squash.definition.*

val tables = listOf(
        BuildInfoTable,
        SwitchesInfoTable,
        InfrastructureInfoTable,
        ExecutionInfoTable,
        TaskInfoTable,
        TaskStateInfoTable,
        FailureInfoTable,
        OutOfDateReasonTable,
        ChangeTable,
        BuildResultInfoTable
)

open class TableDefinitionWithId(name: String? = null) : TableDefinition(name) {
    val id = integer("id").autoIncrement().primaryKey()
}

object BuildInfoTable : TableDefinitionWithId() {
    val project = varchar("project_name", 100)
    val switchesInfoId = integer("switches_info_id")
    val infrastructureInfoId = integer("infrastructure_info_id")
    val executionInfoId = integer("execution_info_id")
    val buildResultInfoId = integer("build_result_info_id")
}

object SwitchesInfoTable : TableDefinitionWithId() {
    val isBuildCacheEnabled = bool("is_build_cache_enabled")
    val isConfigureOnDemand = bool("is_configure_on_demand")
    val isContinueOnFailure = bool("is_continue_on_failure")
    val isContinuous = bool("is_continuous")
    val isDryRun = bool("is_dry_run")
    val isOffline = bool("is_offline")
    val isParallelProjectExecutionEnabled = bool("is_parallel")
    val isRerunTasks = bool("is_rerun_tasks")
    val isRefreshDependencies = bool("is_refresh_deps")
}

object InfrastructureInfoTable : TableDefinitionWithId() {
    val osName = varchar("os_name", 30).nullable()
    val osVersion = varchar("os_version", 20).nullable()
    val cpuCores = integer("cpu_cores")
    val maxGradleWorkers = integer("max_gradle_workers")
    val jreName = varchar("jre_name", 100).nullable()
    val jreVersion = varchar("jre_version", 100).nullable()
    val vmName = varchar("vm_name", 100).nullable()
    val vmVendor = varchar("vm_vendor", 50).nullable()
    val vmVersion = varchar("vm_version", 30).nullable()
    val maxVMHeapSize = long("max_vm_heap_size")
    val locale = varchar("locale", 50)
    val defaultCharset = varchar("charset", 20)
}

object ExecutionInfoTable : TableDefinitionWithId() {
    val executionStartedDate = long("execution_started_date")
}

object TaskInfoTable : TableDefinitionWithId() {
    val executionInfoId = integer("execution_info_id")
    val path = varchar("path", 1024)
    val startedOffset = long("started_offset")
    val duration = long("duration")
    val taskStateInfoId = integer("task_state_info_id")
}

object TaskStateInfoTable : TableDefinitionWithId() {
    val executed = bool("executed")
    val failureId = integer("failure_id").nullable()
    val didWork = bool("did_work")
    val skipped = bool("skipped")
    val skipMessage = varchar("skip_message", 50).nullable()
    val upToDate = bool("up_to_date")
    val outOfDateReasonId = integer("out_of_date_reason_id").nullable()
    val noSource = bool("no_source")
}

object BuildResultInfoTable : TableDefinitionWithId(){
    val action = enumeration<BuildResultInfo.Action>("action")
    val failureId = integer("failure_id").nullable()
}

object FailureInfoTable : TableDefinitionWithId() {
    val message = varchar("message", 200)
    val cause = varchar("cause", 200)
}

object OutOfDateReasonTable : TableDefinitionWithId() {
    val reason = enumeration<OutOfDateReason.Reason>("reason")
}

object ChangeTable : TableDefinitionWithId() {
    val outOfDateReasonId = integer("out_of_date_id")
    val name = varchar("name", 1024)
    val changeType = enumeration<Change.ChangeType>("change_type")
    val inputType = enumeration<Change.InputType>("input_type")
}