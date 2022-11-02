package org.vorpal.research.kex.plugin.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*

class KexOptionsConfigurable : BoundConfigurable("Kex") {

    private var state = KexOptionsStateComponent.instance.state

    override fun createPanel(): DialogPanel {
        return panel {
            row("Runtime dependencies path:") {
                textField().bindText(state::runtimeDepsPath)
            }
            row("Libraries path:") {
                textField().bindText(state::libPath)
            }
            row("Runtime version:") {
                textField().bindText(state::rtVersion)
            }
            row("Kex runtime version:") {
                textField().bindText(state::kexRtVersion)
            }
            row("Intrinsics version:") {
                textField().bindText(state::intrinsicsVersion)
            }
            row("JUnit version:") {
                textField().bindText(state::junitVersion)
            }
            row("Output directory:") {
                textField().bindText(state::outputDir)
            }
            row {
                checkBox("Use Java runtime").bindSelected(state::useJavaRuntime)
            }
            row {
                checkBox("Use Kex runtime").bindSelected(state::useKexRuntime)
            }
            row {
                checkBox("Print detailed coverage").bindSelected(state::printDetailedCoverage)
            }
        }
    }
}