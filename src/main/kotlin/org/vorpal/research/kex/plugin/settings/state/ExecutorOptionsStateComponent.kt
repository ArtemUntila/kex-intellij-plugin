package org.vorpal.research.kex.plugin.settings.state

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "ExecutorOptions", storages = [Storage("kex.xml")])
class ExecutorOptionsStateComponent : PersistentStateComponent<ExecutorOptionsStateComponent.ExecutorOptionsState> {

    companion object {
        val instance: ExecutorOptionsStateComponent
            get() = ApplicationManager.getApplication().getService(ExecutorOptionsStateComponent::class.java)
    }

    private var state = ExecutorOptionsState()

    override fun getState(): ExecutorOptionsState {
        return state
    }

    override fun loadState(state: ExecutorOptionsState) {
        this.state = state
    }

    data class ExecutorOptionsState(
        var executorPath: String = "kex-executor/target/kex-executor-0.0.1-jar-with-dependencies.jar",
        var executorConfigPath: String = "kex.ini",
        var executorPolicyPath: String = "kex.policy",
        var numberOfExecutors: Int = 8,
        var masterJvmParams: String = "-Xmx2g",
        var workerJvmParams: String = "-Xmx2g"
    )
}