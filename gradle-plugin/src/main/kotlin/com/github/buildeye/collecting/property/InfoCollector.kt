package com.github.buildeye.collecting.property

abstract class InfoCollector<out V>(propertyName: String) : InfoProperty<V, List<String>>(propertyName)