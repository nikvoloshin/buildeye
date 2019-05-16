package com.github.buildeye.infos

abstract class OutOfDateReason(val reason: Reason) {
    enum class Reason {
        UNKNOWN, INPUTS_CHANGE
    }
}

data class InputsChange(val changes: Collection<Change>) : OutOfDateReason(Reason.INPUTS_CHANGE)

class Unknown: OutOfDateReason(Reason.UNKNOWN)

data class Change(
        val name: String,
        val changeType: ChangeType,
        val inputType: InputType
) {
    enum class ChangeType {
        NEW, REMOVED, CHANGED
    }

    enum class InputType {
        PROPERTY, FILE
    }
}
