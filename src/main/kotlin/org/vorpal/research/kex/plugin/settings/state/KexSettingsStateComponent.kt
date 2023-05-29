package org.vorpal.research.kex.plugin.settings.state

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "KexSettings", storages = [Storage("kex.xml")])
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
        var outputDir: String = "",
        var dockerImage: String = "artemuntila/kex-plugin",
        var dockerRemove: Boolean = true,
        var guiConnectionTimeout: Int = 60
    )
}