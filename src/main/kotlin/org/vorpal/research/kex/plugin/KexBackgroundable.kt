package org.vorpal.research.kex.plugin

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import org.vorpal.research.kex.plugin.command.DockerKillCommand
import org.vorpal.research.kex.plugin.command.DockerRunCommand
import org.vorpal.research.kex.plugin.util.onCanceled

class KexBackgroundable(
    project: Project, title: String,
    private val dockerRunCommand: DockerRunCommand,
    private val consoleView: ConsoleView
) : Backgroundable(project, title) {

    override fun run(indicator: ProgressIndicator) {
        indicator.onCanceled(1000) {
            val dockerKillCommand = DockerKillCommand(dockerRunCommand.containerName!!)
            ProcessBuilder(dockerKillCommand.args()).start().waitFor()
        }

        val processHandler = OSProcessHandler(GeneralCommandLine(dockerRunCommand.args()))
        consoleView.attachToProcess(processHandler)
        processHandler.startNotify()
        processHandler.waitFor()
    }
}