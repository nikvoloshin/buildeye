package com.github.buildeye.infos

data class FailureInfo(val message: String, val cause: String) {
    companion object {
        fun of(failure: Throwable) = FailureInfo(
                failure.message ?: "",
                failure.cause?.toString() ?: ""
        )

        fun ofNullable(failure: Throwable?) = when (failure) {
            null -> null
            else -> of(failure)
        }
    }
}