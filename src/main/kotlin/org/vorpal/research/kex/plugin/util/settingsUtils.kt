package org.vorpal.research.kex.plugin.util

import org.vorpal.research.kex.plugin.settings.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

fun main() {
    val list = getOptionsArgsList("kex", KexOptionsStateComponent.instance.state)
}

fun getAllOptionsArgsList() =
    getKexOptionsArgsList() + getTestGenOptionsArgsList() + getConcolicOptionsArgsList() + getExecutorOptionsArgsList()

fun getKexOptionsArgsList() = getOptionsArgsList("kex", KexOptionsStateComponent.instance.state)

fun getTestGenOptionsArgsList() = getOptionsArgsList("testGen", TestGenOptionsStateComponent.instance.state)

fun getConcolicOptionsArgsList() = getOptionsArgsList("concolic", ConcolicOptionsStateComponent.instance.state)

fun getExecutorOptionsArgsList() = getOptionsArgsList("executor", ExecutorOptionsStateComponent.instance.state)

fun getKexOutput(): String? {
    val state = KexSettingsStateComponent.instance.state
    return if (state.kexOutput) state.outputDir else null
}

private fun getOptionsArgsList(section: String, instance: Any): List<String> {
    val map = getPropertyValueMap(instance)
    val optionsList = OptionsList(section)
    for ((option, value) in map) {
        optionsList.add(option, value)
    }
    return optionsList.list
}

private fun getPropertyValueMap(instance: Any): Map<String, Any> {
    val properties = instance::class.memberProperties.map { it as KProperty1<Any, Any> }
    return properties.associateBy({ it.name }, { it.get(instance) })
}