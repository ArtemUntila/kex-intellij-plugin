package org.vorpal.research.kex.plugin.runner

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import org.vorpal.research.kex.plugin.command.Command
import org.vorpal.research.kex.plugin.util.onCanceled

class CommandBackgroundable(
    project: Project, title: String,
    private val runCommand: Command,
    private val cancelCommand: Command? = null,
    private val consoleView: ConsoleView? = null
) : Backgroundable(project, title) {

    private var _isRunning = false

    val isRunning: Boolean
        get() = _isRunning

    override fun run(indicator: ProgressIndicator) {
        _isRunning = true

        indicator.onCanceled {
            cancelCommand?.let {
                ProcessBuilder(it.args()).start().waitFor()
            }
        }

        val processHandler = OSProcessHandler(GeneralCommandLine(runCommand.args()))
        consoleView?.attachToProcess(processHandler)
        processHandler.startNotify()
        processHandler.waitFor()

        _isRunning = false
    }
}