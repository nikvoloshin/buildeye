package com.github.buildeye.inputs.snapshot

import java.io.Serializable

data class InputsSnapshot(
        val properties: Map<String, Input>,
        val inputFiles: Map<String, String>
) : Serializable

data class Input(val input: Any?)