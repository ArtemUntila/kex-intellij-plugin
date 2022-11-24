package org.vorpal.research.kex.plugin.util

import org.vorpal.research.kex.plugin.settings.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

fun getAllOptionArgs() =
    getKexOptionArgs() + getTestGenOptionArgs() + getConcolicOptionArgs() + getExecutorOptionArgs()

fun getKexOptionArgs() = getOptionArgs("kex", KexOptionsStateComponent.instance.state)

fun getTestGenOptionArgs() = getOptionArgs("testGen", TestGenOptionsStateComponent.instance.state)

fun getConcolicOptionArgs() = getOptionArgs("concolic", ConcolicOptionsStateComponent.instance.state)

fun getExecutorOptionArgs() = getOptionArgs("executor", ExecutorOptionsStateComponent.instance.state)

fun getKexOutput(): String? {
    val state = KexSettingsStateComponent.instance.state
    return if (state.kexOutput) state.outputDir else null
}

private fun getOptionArgs(section: String, instance: Any): List<String> {
    val map = getPropertyValueMap(instance)
    val options = OptionList(section)
    for ((option, value) in map) {
        options.add(option, value)
    }
    return options.list
}

private fun getPropertyValueMap(instance: Any): Map<String, Any> {
    val properties = instance::class.memberProperties.map { it as KProperty1<Any, Any> }
    return properties.associateBy({ it.name }, { it.get(instance) })
}