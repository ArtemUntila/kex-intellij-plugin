package org.vorpal.research.kex.plugin.settings.state

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "KexOptions", storages = [Storage("kex.xml")])
class KexOptionsStateComponent : PersistentStateComponent<KexOptionsStateComponent.KexOptionsState> {

    companion object {
        val instance: KexOptionsStateComponent
            get() = ApplicationManager.getApplication().getService(KexOptionsStateComponent::class.java)
    }

    private var state = KexOptionsState()

    override fun getState(): KexOptionsState {
        return state
    }

    override fun loadState(state: KexOptionsState) {
        this.state = state
    }

    data class KexOptionsState (
        var runtimeDepsPath: String = "runtime-deps/",
        var libPath: String = "lib/",
        var rtVersion: String = "1.8",
        var kexRtVersion: String = "0.0.1",
        var intrinsicsVersion: String = "0.1.0",
        var junitVersion: String = "4.13.2",
//        var outputDir: String = "temp/",
        var useJavaRuntime: Boolean = true,
        var useKexRuntime: Boolean = true,
        var computeCoverage: Boolean = true,
        var computeSaturationCoverage: Boolean = false,
        var printDetailedCoverage: Boolean = true,
        var useReflectionInfo: Boolean = false
    )
}