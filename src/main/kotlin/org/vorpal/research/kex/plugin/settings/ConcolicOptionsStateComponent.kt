package org.vorpal.research.kex.plugin.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "org.vorpal.research.kex.plugin.settings.ConcolicOptionsState", storages = [Storage("KexSettings.xml")])
class ConcolicOptionsStateComponent : PersistentStateComponent<ConcolicOptionsStateComponent.ConcolicOptionsState> {

    companion object {
        val instance: ConcolicOptionsStateComponent
            get() = ApplicationManager.getApplication().getService(ConcolicOptionsStateComponent::class.java)
    }

    private var state = ConcolicOptionsState()

    override fun getState(): ConcolicOptionsState {
        return state
    }

    override fun loadState(state: ConcolicOptionsState) {
        this.state = state
    }

    data class ConcolicOptionsState(
        var timeLimit: Int = 120000,
        var numberOfExecutors: Int = 16,
        var searchStrategy: String = "cgs"
    )
}