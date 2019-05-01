package com.github.buildeye.infos

sealed class OutOfDateReason

object Unknown : OutOfDateReason()

data class InputsChange(val changes: Iterable<Change>) : OutOfDateReason()

data class Change(
        val name: String,
        val changeType: ChangeType,
        val inputType: InputType
) {
    enum class ChangeType {
        NEW, CHANGED
    }

    enum class InputType {
        PROPERTY, FILE
    }
}
