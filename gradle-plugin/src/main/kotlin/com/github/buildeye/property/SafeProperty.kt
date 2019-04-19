package com.github.buildeye.property

interface SafeProperty<out V, out F> {
    fun get() : EitherValueFailure<V, F>
}