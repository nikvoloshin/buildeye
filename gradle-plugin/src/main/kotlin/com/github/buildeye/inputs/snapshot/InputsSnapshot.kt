package com.github.buildeye.inputs.snapshot

import java.io.Serializable

data class InputsSnapshot(
        val properties: Map<String, Any?>,
        val inputFiles: Map<String, String>
) : Serializable {
    companion object {
        private const val serialVersionUID = 0L
    }
}