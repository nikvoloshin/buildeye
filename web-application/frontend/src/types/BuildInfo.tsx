export interface IndexedBuildInfo {
    id: number
    buildInfo: BuildInfo
}

export interface BuildInfo {
    project: string
    switchesInfo: SwitchesInfo
    infrastructureInfo: InfrastructureInfo
    executionInfo: ExecutionInfo
    buildResultInfo: BuildResultInfo
}

export interface SwitchesInfo {
    isBuildCacheEnabled: boolean
    isConfigureOnDemand: boolean
    isContinueOnFailure: boolean
    isContinuous: boolean
    isDryRun: boolean
    isOffline: boolean
    isParallelProjectExecutionEnabled: boolean
    isRerunTasks: boolean
    isRefreshDependencies: boolean    
}

export interface InfrastructureInfo {
    osName?: string
    osVersion?: string
    cpuCores: number
    maxGradleWorkers: number
    jreName?: string
    jreVersion?: string
    vmName?: string
    vmVendor?: string
    vmVersion?: string
    maxVMHeapSize: number
    locale: string
    defaultCharset: string
}

export interface ExecutionInfo {
    executionStartedDate: number
    taskInfos: Array<TaskInfo>
}

export interface BuildResultInfo {
    action: BuildAction
    failure?: FailureInfo
}

export enum BuildAction {
    BUILD = "BUILD",
    CONFIGURE = "CONFIGURE"
}

export interface TaskInfo {
    path: string,
    startedOffset: number,
    duration: number,
    taskStateInfo: TaskStateInfo
}

export interface TaskStateInfo {
    executed: boolean
    failure?: FailureInfo,
    didWork: boolean,
    skipped: boolean,
    skipMessage?: string,
    upToDate: boolean,
    outOfDateReason?: OutOfDateReason,
    noSource: boolean
}

export interface FailureInfo {
    message: string,
    cause: string
}

export interface OutOfDateReason {
    reason: Reason
    inputsChange?: Array<Change>
}

export enum Reason {
    UNKNOWN, INPUTS_CHANGE
}

export interface Change {
    name: string
    changeType: ChangeType
    inputType: InputType
}

export enum ChangeType {
    NEW, REMOVED, CHANGED
}

export enum InputType {
    PROPERTY, FILE
}
