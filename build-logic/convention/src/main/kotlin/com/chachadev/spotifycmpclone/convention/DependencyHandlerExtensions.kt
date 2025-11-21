package com.chachadev.spotifycmpclone.convention

import org.gradle.kotlin.dsl.DependencyHandlerScope

internal fun DependencyHandlerScope.addAll(
    configurationName: String,
    dependencyNotations: Iterable<Any>
) {
    dependencyNotations.forEach { notation ->
        add(configurationName, notation)
    }
}

internal fun DependencyHandlerScope.addAll(
    configurationName: String,
    vararg dependencyNotations: Any
) {
    addAll(configurationName, dependencyNotations.asIterable())
}