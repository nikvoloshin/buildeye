package com.github.buildeye.storage

import com.github.buildeye.infos.*
import org.jetbrains.squash.connection.DatabaseConnection
import org.jetbrains.squash.connection.transaction
import org.jetbrains.squash.definition.Column
import org.jetbrains.squash.definition.TableDefinition
import org.jetbrains.squash.expressions.Expression
import org.jetbrains.squash.expressions.eq
import org.jetbrains.squash.expressions.literal
import org.jetbrains.squash.query.from
import org.jetbrains.squash.query.where
import org.jetbrains.squash.results.ResultRow
import org.jetbrains.squash.results.get
import org.jetbrains.squash.statements.InsertValuesStatement
import org.jetbrains.squash.statements.fetch
import org.jetbrains.squash.statements.insertInto
import org.jetbrains.squash.statements.values

class BuildInfosDatabase(private val db: DatabaseConnection) : BuildInfosStorage, AutoCloseable {
    init {
        db.transaction {
            databaseSchema().create(tables)
        }
    }

    override fun fetchBuildInfo(id: Int) = BuildInfoTable.queryRowById(id) {
        BuildInfoTableModel(
                get { this.id },
                get { project },
                get { switchesInfoId },
                get { infrastructureInfoId },
                get { executionInfoId },
                get { buildResultInfoId }
        )
    }.let {
        IndexedBuildInfo(id, BuildInfo(
                it.project,
                fetchSwitchesInfo(it.switchesInfoId),
                fetchInfrastructureInfo(it.infrastructureInfoId),
                fetchExecutionInfo(it.executionInfoId),
                fetchBuildResultInfo(it.buildResultInfoId)
        ))
    }

    override fun fetchAllBuildInfos() = BuildInfoTable.queryAllRows { get { id } }.map(::fetchBuildInfo)

    private fun fetchSwitchesInfo(id: Int) = SwitchesInfoTable.queryRowById(id) {
        SwitchesInfo(
                get { isBuildCacheEnabled },
                get { isConfigureOnDemand },
                get { isContinueOnFailure },
                get { isContinuous },
                get { isDryRun },
                get { isOffline },
                get { isParallelProjectExecutionEnabled },
                get { isRerunTasks },
                get { isRefreshDependencies }
        )
    }

    private fun fetchInfrastructureInfo(id: Int) = InfrastructureInfoTable.queryRowById(id) {
        InfrastructureInfo(
                get { osName },
                get { osVersion },
                get { cpuCores },
                get { maxGradleWorkers },
                get { jreName },
                get { jreVersion },
                get { vmName },
                get { vmVendor },
                get { vmVersion },
                get { maxVMHeapSize },
                get { locale },
                get { defaultCharset }
        )
    }

    private fun fetchExecutionInfo(id: Int) = ExecutionInfoTable.queryRowById(id) {
        ExecutionInfoTableModel(
                get { this.id },
                get { executionStartedDate },
                get { duration }
        )
    }.let {
        ExecutionInfo(
                it.executionStartedDate,
                it.duration,
                fetchTaskInfos(it.id)
        )
    }

    private fun fetchTaskInfos(executionInfoId: Int) =
            TaskInfoTable.queryRowsBy({ it.executionInfoId eq executionInfoId }) {
                TaskInfoTableModel(
                        get { id },
                        get { this.executionInfoId },
                        get { path },
                        get { startedOffset },
                        get { duration },
                        get { taskStateInfoId }
                )
            }.map {
                TaskInfo(
                        it.path,
                        it.startedOffset,
                        it.duration,
                        fetchTaskStateInfo(it.taskStateInfoId)
                )
            }

    private fun fetchTaskStateInfo(id: Int) = TaskStateInfoTable.queryRowById(id) {
        TaskStateInfoTableModel(
                get { this.id },
                get { executed },
                get { failureId },
                get { didWork },
                get { skipped },
                get { skipMessage },
                get { upToDate },
                get { outOfDateReasonId },
                get { noSource }
        )
    }.let {
        TaskStateInfo(
                it.executed,
                it.failureId?.let(::fetchFailureInfo),
                it.didWork,
                it.skipped,
                it.skipMessage,
                it.upToDate,
                it.outOfDateReasonId?.let(::fetchOutOfDateReason),
                it.noSource
        )
    }

    private fun fetchOutOfDateReason(id: Int) = OutOfDateReasonTable.queryRowById(id) {
        OutOfDateReasonTableModel(
                get { this.id },
                get { reason }
        )
    }.let {
        when (it.reason) {
            OutOfDateReason.Reason.UNKNOWN -> Unknown()
            OutOfDateReason.Reason.INPUTS_CHANGE -> InputsChange(fetchChanges(id))
        }
    }

    private fun fetchChanges(outOfDateReasonId: Int) =
            ChangeTable.queryRowsBy({ it.outOfDateReasonId eq outOfDateReasonId }) {
                Change(
                        get { name },
                        get { changeType },
                        get { inputType }
                )
            }

    private fun fetchFailureInfo(id: Int) = FailureInfoTable.queryRowById(id) {
        FailureInfo(
                get { message },
                get { cause }
        )
    }

    private fun fetchBuildResultInfo(id: Int) = BuildResultInfoTable.queryRowById(id) {
        BuildResultInfoTableModel(
                get { this.id },
                get { action },
                get { failureId }
        )
    }.let {
        BuildResultInfo(
                it.action,
                it.failureId?.let(::fetchFailureInfo)
        )
    }

    override fun insertBuildInfo(buildInfo: BuildInfo) = BuildInfoTable.insert {
        it[project] = buildInfo.project
        it[switchesInfoId] = insertSwitchesInfo(buildInfo.switchesInfo)
        it[infrastructureInfoId] = insertInfrastructureInfo(buildInfo.infrastructureInfo)
        it[executionInfoId] = insertExecutionInfo(buildInfo.executionInfo)
        it[buildResultInfoId] = insertBuildResultInfo(buildInfo.buildResultInfo)
    }

    private fun insertBuildResultInfo(buildResultInfo: BuildResultInfo) = BuildResultInfoTable.insert {
        it[action] = buildResultInfo.action
        it[failureId] = buildResultInfo.failure?.let(::insertFailureInfo)
    }

    private fun insertExecutionInfo(executionInfo: ExecutionInfo) = ExecutionInfoTable.insert {
        it[executionStartedDate] = executionInfo.executionStartedDate
        it[duration] = executionInfo.duration
    }.also { executionInfoId ->
        executionInfo.taskInfos.forEach { insertTaskInfo(executionInfoId, it) }
    }

    private fun insertTaskInfo(executionId: Int, taskInfo: TaskInfo) = TaskInfoTable.insert {
        it[executionInfoId] = executionId
        it[path] = taskInfo.path
        it[startedOffset] = taskInfo.startedOffset
        it[duration] = taskInfo.duration
        it[taskStateInfoId] = insertTaskStateInfo(taskInfo.taskStateInfo)
    }

    private fun insertTaskStateInfo(taskStateInfo: TaskStateInfo) = TaskStateInfoTable.insert {
        it[executed] = taskStateInfo.executed
        it[failureId] = taskStateInfo.failure?.let(::insertFailureInfo)
        it[didWork] = taskStateInfo.didWork
        it[skipped] = taskStateInfo.skipped
        it[skipMessage] = taskStateInfo.skipMessage
        it[upToDate] = taskStateInfo.upToDate
        it[outOfDateReasonId] = taskStateInfo.outOfDateReason?.let(::insertOutOfDateReason)
        it[noSource] = taskStateInfo.noSource
    }

    private fun insertOutOfDateReason(outOfDateReason: OutOfDateReason) = OutOfDateReasonTable.insert {
        it[reason] = outOfDateReason.reason
    }.also { outOfDateReasonId ->
        if (outOfDateReason is InputsChange) {
            outOfDateReason.changes.forEach { insertChangeInfo(outOfDateReasonId, it) }
        }
    }

    private fun insertChangeInfo(outOfDateId: Int, change: Change) = ChangeTable.insert {
        it[outOfDateReasonId] = outOfDateId
        it[name] = change.name
        it[changeType] = change.changeType
        it[inputType] = change.inputType
    }

    private fun insertFailureInfo(failureInfo: FailureInfo) = FailureInfoTable.insert {
        it[message] = failureInfo.message
        it[cause] = failureInfo.cause
    }

    private fun insertInfrastructureInfo(infrastructureInfo: InfrastructureInfo) = InfrastructureInfoTable.insert {
        it[osName] = infrastructureInfo.osName
        it[osVersion] = infrastructureInfo.osVersion
        it[cpuCores] = infrastructureInfo.cpuCores
        it[maxGradleWorkers] = infrastructureInfo.maxGradleWorkers
        it[jreName] = infrastructureInfo.jreName
        it[jreVersion] = infrastructureInfo.jreVersion
        it[vmName] = infrastructureInfo.vmName
        it[vmVendor] = infrastructureInfo.vmVendor
        it[vmVersion] = infrastructureInfo.vmVersion
        it[maxVMHeapSize] = infrastructureInfo.maxVMHeapSize
        it[locale] = infrastructureInfo.locale
        it[defaultCharset] = infrastructureInfo.defaultCharset
    }

    private fun insertSwitchesInfo(switchesInfo: SwitchesInfo) = SwitchesInfoTable.insert {
        it[isBuildCacheEnabled] = switchesInfo.isBuildCacheEnabled
        it[isConfigureOnDemand] = switchesInfo.isConfigureOnDemand
        it[isContinueOnFailure] = switchesInfo.isContinueOnFailure
        it[isContinuous] = switchesInfo.isContinuous
        it[isDryRun] = switchesInfo.isDryRun
        it[isOffline] = switchesInfo.isOffline
        it[isParallelProjectExecutionEnabled] = switchesInfo.isParallelProjectExecutionEnabled
        it[isRerunTasks] = switchesInfo.isRerunTasks
        it[isRefreshDependencies] = switchesInfo.isRefreshDependencies
    }

    override fun close() = db.close()

    private fun <T : TableDefinitionWithId> T.insert(body: T.(InsertValuesStatement<T, Unit>) -> Unit) = db.transaction {
        this@insert.let { table ->
            insertInto(table).values(body).fetch(table.id).execute()
        }
    }

    private fun <T : TableDefinitionWithId, R> T.queryRowById(id: Int, block: RowWithTable<T>.() -> R) =
            queryRowBy({ this@queryRowById.id eq id }, block)

    private fun <T : TableDefinition, R> T.queryAllRows(
            block: RowWithTable<T>.() -> R
    ) = rowsTransactionBy({ literal(true) }, block, Sequence<R>::toList)

    private fun <T : TableDefinition, R> T.queryRowsBy(
            rowPredicate: (T) -> Expression<Boolean>,
            block: RowWithTable<T>.() -> R
    ) = rowsTransactionBy(rowPredicate, block, Sequence<R>::toList)

    private fun <T : TableDefinition, R> T.queryRowBy(
            rowPredicate: (T) -> Expression<Boolean>,
            block: RowWithTable<T>.() -> R
    ) = rowsTransactionBy(rowPredicate, block, Sequence<R>::single)

    private fun <T : TableDefinition, R, U> T.rowsTransactionBy(
            rowPredicate: (T) -> Expression<Boolean>,
            block: RowWithTable<T>.() -> R,
            collector: Sequence<R>.() -> U
    ) = db.transaction {
        this@rowsTransactionBy.let { table ->
            from(table)
                    .where { rowPredicate(table) }
                    .execute()
                    .map { it.bindTable(table) }
                    .map(block)
                    .collector()
        }
    }

    private fun <T : TableDefinition> ResultRow.bindTable(table: T) = RowWithTable(table, this)

    private class RowWithTable<T : TableDefinition>(val table: T, val row: ResultRow) {
        inline fun <reified V> get(getter: T.() -> Column<V>) = row[table.getter()]
    }
}
