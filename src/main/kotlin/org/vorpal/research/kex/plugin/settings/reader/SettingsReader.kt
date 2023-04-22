package org.vorpal.research.kex.plugin.settings.reader

import org.vorpal.research.kex.plugin.settings.state.*
import org.vorpal.research.kex.plugin.util.OptionList
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

object SettingsReader {

    val kexOptions: List<String>
        get() = getOptions("kex", KexOptionsStateComponent.instance.state)

    val testGenOptions: List<String>
        get() = getOptions("testGen", TestGenOptionsStateComponent.instance.state)

    val concolicOptions: List<String>
        get() = getOptions("concolic", ConcolicOptionsStateComponent.instance.state)

    val executorOptions: List<String>
        get() = getOptions("executor", ExecutorOptionsStateComponent.instance.state)

    private fun getOptions(section: String, instance: Any): List<String> {
        val map = getPropertyValueMap(instance)
        val options = OptionList(section)
        for ((option, value) in map) {
            options.addOption(option, value)
        }
        return options
    }

    private fun getPropertyValueMap(instance: Any): Map<String, Any> {
        val properties = instance::class.memberProperties.map { it as KProperty1<Any, Any> }
        return properties.associateBy({ it.name }, { it.get(instance) })
    }

    fun getKexOutput(): String? {
        val state = KexSettingsStateComponent.instance.state
        return if (state.kexOutput) state.outputDir else null
    }
}