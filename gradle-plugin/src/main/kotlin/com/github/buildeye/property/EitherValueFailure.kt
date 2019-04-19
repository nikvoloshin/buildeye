package com.github.buildeye.property

sealed class EitherValueFailure<out V, out F>

class Value<out V>(private val value: V) : EitherValueFailure<V, Nothing>() {
    fun get(): V = value
}

open class Failure<out F>(private val failure: F) : EitherValueFailure<Nothing, F>() {
    fun getFailure(): F = failure
}