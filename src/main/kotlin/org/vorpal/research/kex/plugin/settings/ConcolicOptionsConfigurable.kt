package org.vorpal.research.kex.plugin.settings

import com.intellij.codeInspection.javaDoc.JavadocUIUtil.bindItem
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindIntText
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel

class ConcolicOptionsConfigurable : BoundConfigurable("Concolic") {

    private val state = ConcolicOptionsStateComponent.instance.state

    override fun createPanel(): DialogPanel {
        return panel {
            row("Time limit:") {
                intTextField().bindIntText(state::timeLimit)
            }
            row("Number of executors:") {
                intTextField().bindIntText(state::numberOfExecutors)
            }
            row("Search strategy:") {
                comboBox(listOf("bfs", "cgs")).bindItem(state::searchStrategy)
            }
        }
    }
}