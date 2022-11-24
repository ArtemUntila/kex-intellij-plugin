package org.vorpal.research.kex.plugin.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*

class ExecutorOptionsConfigurable : BoundConfigurable("Executor") {

    private val state = ExecutorOptionsStateComponent.instance.state

    override fun createPanel(): DialogPanel {
        return panel {
            row("Executor path:") {
                textField().columns(COLUMNS_LARGE).bindText(state::executorPath)
            }
            row("Executor config path:") {
                textField().bindText(state::executorConfigPath)
            }
            row("Executor policy path:") {
                textField().bindText(state::executorPolicyPath)
            }
            row("Number of workers:") {
                intTextField().bindIntText(state::numberOfExecutors)
            }
            row("Master JVM params:") {
                textField().bindText(state::masterJvmParams)
            }
            row("Worker JVM params:") {
                textField().bindText(state::workerJvmParams)
            }
        }
    }
}