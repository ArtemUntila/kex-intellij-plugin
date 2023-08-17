package org.vorpal.research.kex.plugin.settings

import org.vorpal.research.kex.plugin.settings.state.*
import org.vorpal.research.kex.plugin.util.Option
import org.vorpal.research.kex.plugin.util.Section
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

object SettingsReader {

    private val kexSettingsState = KexSettingsStateComponent.instance.state
    private val kexOptionsState = KexOptionsStateComponent.instance.state
    private val testGenOptionsState = TestGenOptionsStateComponent.instance.state
    private val concolicOptionsState = ConcolicOptionsStateComponent.instance.state
    private val executorOptionsState = ExecutorOptionsStateComponent.instance.state

    val kexOptions: Map<Option, String>
        get() = getOptions(Section.kex, kexOptionsState)

    val testGenOptions: Map<Option, String>
        get() = getOptions(Section.testGen, testGenOptionsState)

    val concolicOptions: Map<Option, String>
        get() = getOptions(Section.concolic, concolicOptionsState)

    val executorOptions: Map<Option, String>
        get() = getOptions(Section.executor, executorOptionsState)

    val kexOutputDir: String?
        get() = if (kexSettingsState.kexOutput) kexSettingsState.outputDir else null

    val testsDir: String
        get() = testGenOptionsState.testsDir

    val dockerImage: String
        get() = kexSettingsState.dockerImage

    val dockerRemove: Boolean
        get() = kexSettingsState.dockerRemove

    val guiConnectionTimeout: Int
        get() = kexSettingsState.guiConnectionTimeout

    private fun getOptions(section: Section, instance: Any): Map<Option, String> {
        return getPropertyValueMap(instance)
            .mapKeys { Option(section, it.key) }
            .mapValues { "${it.value}" }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getPropertyValueMap(instance: Any): Map<String, Any> {
        val properties = instance::class.memberProperties.map { it as KProperty1<Any, Any> }
        return properties.associateBy({ it.name }, { it.get(instance) })
    }
}