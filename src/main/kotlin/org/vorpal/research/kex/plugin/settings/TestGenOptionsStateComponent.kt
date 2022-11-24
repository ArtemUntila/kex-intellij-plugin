package org.vorpal.research.kex.plugin.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "org.vorpal.research.kex.plugin.settings.TestGenOptionsState", storages = [Storage("KexSettings.xml")])
class TestGenOptionsStateComponent : PersistentStateComponent<TestGenOptionsStateComponent.TestGenOptionsState> {

    companion object {
        val instance: TestGenOptionsStateComponent
            get() = ApplicationManager.getApplication().getService(TestGenOptionsStateComponent::class.java)
    }

    private var state = TestGenOptionsState()

    override fun getState(): TestGenOptionsState {
        return state
    }

    override fun loadState(state: TestGenOptionsState) {
        this.state = state
    }

    data class TestGenOptionsState(
        var enabled: Boolean = true,
        var testsDir: String = "tests/",
        var accessLevel: String = "private",
        var testCaseLanguage: String = "java",
        var generateSetup: Boolean = true,
        var logJUnit: Boolean = false,
        var testTimeout: Int = 100000
    )
}