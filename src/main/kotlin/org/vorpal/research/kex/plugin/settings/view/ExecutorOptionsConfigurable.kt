package org.vorpal.research.kex.plugin.settings.view

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*
import org.vorpal.research.kex.plugin.settings.state.ExecutorOptionsStateComponent

class ExecutorOptionsConfigurable : BoundConfigurable("Executor") {

    private val state = ExecutorOptionsStateComponent.instance.state

    override fun createPanel(): DialogPanel {
        return panel {
            row("Executor path:") {
                textField().bindText(state::executorPath).align(AlignX.FILL)
            }
            row("Executor config path:") {
                textField().bindText(state::executorConfigPath).align(AlignX.FILL)
            }
            row("Executor policy path:") {
                textField().bindText(state::executorPolicyPath).align(AlignX.FILL)
            }
            row("Number of workers:") {
                intTextField(IntRange(1, 32)).bindIntText(state::numberOfWorkers)
            }
            row("Master JVM params:") {
                textField().bindText(state::masterJvmParams).align(AlignX.FILL)
            }
            row("Worker JVM params:") {
                textField().bindText(state::workerJvmParams).align(AlignX.FILL)
            }
        }
    }
}