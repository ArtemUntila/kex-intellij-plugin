package org.vorpal.research.kex.plugin.settings.reader

import org.vorpal.research.kex.plugin.settings.state.*
import org.vorpal.research.kex.plugin.util.Option
import org.vorpal.research.kex.plugin.util.Section
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

object SettingsReader {

    val allOptions: List<Option>
        get() = kexOptions + testGenOptions + concolicOptions + executorOptions

    val kexOptions: List<Option>
        get() = getOptions(Section.kex, KexOptionsStateComponent.instance.state)

    val testGenOptions: List<Option>
        get() = getOptions(Section.testGen, TestGenOptionsStateComponent.instance.state)

    val concolicOptions: List<Option>
        get() = getOptions(Section.concolic, ConcolicOptionsStateComponent.instance.state)

    val executorOptions: List<Option>
        get() = getOptions(Section.executor, ExecutorOptionsStateComponent.instance.state)

    val kexOutputDir: String?
        get() {
            val state = KexSettingsStateComponent.instance.state
            return if (state.kexOutput) state.outputDir else null
        }

    private fun getOptions(section: Section, instance: Any): List<Option> {
        val map = getPropertyValueMap(instance)
        return map.toList().map { (name, value) ->
            Option(section, name, value)
        }
    }

    private fun getPropertyValueMap(instance: Any): Map<String, Any> {
        val properties = instance::class.memberProperties.map { it as KProperty1<Any, Any> }
        return properties.associateBy({ it.name }, { it.get(instance) })
    }
}