package org.vorpal.research.kex.plugin.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "org.vorpal.research.kex.plugin.settings.KexSettingsState", storages = [Storage("KexSettings.xml")])
class KexSettingsStateComponent : PersistentStateComponent<KexSettingsStateComponent.KexSettingsState> {

    companion object {
        val instance: KexSettingsStateComponent
            get() = ApplicationManager.getApplication().getService(KexSettingsStateComponent::class.java)
    }

    private var state = KexSettingsState()

    override fun getState(): KexSettingsState {
        return state
    }

    override fun loadState(state: KexSettingsState) {
        this.state = state
    }

    data class KexSettingsState(
        var kexOutput: Boolean = false,
        var outputDir: String = ""
    )
}