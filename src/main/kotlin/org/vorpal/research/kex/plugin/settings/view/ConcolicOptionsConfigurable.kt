package org.vorpal.research.kex.plugin.settings.view

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*
import org.vorpal.research.kex.plugin.settings.state.ConcolicOptionsStateComponent

class ConcolicOptionsConfigurable : BoundConfigurable("Concolic") {

    private val state = ConcolicOptionsStateComponent.instance.state

    override fun createPanel(): DialogPanel {
        return panel {
            row("Time limit:") {
                intTextField(IntRange(1, 1_000_000))
                    .bindIntText(state::timeLimit)
                    .gap(RightGap.SMALL)
                label("seconds")
            }
            row("Number of executors:") {
                intTextField(IntRange(1, 32)).bindIntText(state::numberOfExecutors)
            }
            row("Search strategy:") {
                comboBox(listOf("bfs", "cgs")).bindItem(state::searchStrategy.toNullableProperty())
            }
        }
    }
}