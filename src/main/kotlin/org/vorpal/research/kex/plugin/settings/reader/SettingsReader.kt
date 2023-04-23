package org.vorpal.research.kex.plugin.settings.reader

import org.jetbrains.uast.test.common.transform
import org.vorpal.research.kex.plugin.settings.state.*
import org.vorpal.research.kex.plugin.util.Option
import org.vorpal.research.kex.plugin.util.Section
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

object SettingsReader {

    val kexOptions: Map<Option, String>
        get() = getOptions(Section.kex, KexOptionsStateComponent.instance.state)

    val testGenOptions: Map<Option, String>
        get() = getOptions(Section.testGen, TestGenOptionsStateComponent.instance.state)

    val concolicOptions: Map<Option, String>
        get() = getOptions(Section.concolic, ConcolicOptionsStateComponent.instance.state)

    val executorOptions: Map<Option, String>
        get() = getOptions(Section.executor, ExecutorOptionsStateComponent.instance.state)

    val kexOutputDir: String?
        get() {
            val state = KexSettingsStateComponent.instance.state
            return if (state.kexOutput) state.outputDir else null
        }

    private fun getOptions(section: Section, instance: Any): Map<Option, String> {
        return getPropertyValueMap(instance)
            .mapKeys { Option(section, it.key) }
            .mapValues { "${it.value}" }
    }

    private fun getPropertyValueMap(instance: Any): Map<String, Any> {
        val properties = instance::class.memberProperties.map { it as KProperty1<Any, Any> }
        return properties.associateBy({ it.name }, { it.get(instance) })
    }
}