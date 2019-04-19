package com.github.buildeye.collecting.property

import com.github.buildeye.property.EitherValueFailure
import com.github.buildeye.property.Failure
import com.github.buildeye.property.Provider
import com.github.buildeye.property.Value

class InfoProvider<V : Any>(propertyName: String) : Provider<V, String>, InfoProperty<V, String>(propertyName) {
    private lateinit var property: EitherValueFailure<V, String>

    override fun get(): EitherValueFailure<V, String> {
        if (!this::property.isInitialized) {
            this.property = Failure("Trying to get property value when not set")
        }

        return property
    }

    override fun set(value: V) {
        if (!this::property.isInitialized) {
            this.property = Value(value)
        } else if (this.property is Value) {
            log.warn("Setting property value when already set")
            this.property = Value(value)
        } else {
            log.warn("Trying to set property value when there is a failure")
        }
    }
}