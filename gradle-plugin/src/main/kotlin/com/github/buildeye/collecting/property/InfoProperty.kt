package com.github.buildeye.collecting.property

import com.github.buildeye.property.SafeProperty
import org.apache.log4j.Logger

abstract class InfoProperty<out V, out F>(propertyName: String) : SafeProperty<V, F> {
    protected val log: Logger = Logger.getLogger("${propertyName.capitalize()} property")
}