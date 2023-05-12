package org.vorpal.research.kex.plugin

import com.intellij.openapi.module.Module
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import org.vorpal.research.kex.plugin.command.DockerKillCommand
import org.vorpal.research.kex.plugin.tw.ToolWindowHelper
import org.vorpal.research.kex.plugin.util.getClasspathList
import org.vorpal.research.kex.plugin.util.getTestDirPath
import org.vorpal.research.kex.plugin.util.onBuildSuccess

class KexRunner(private val project: Project) {

    fun run(module: Module, target: String, attachConsoleView: Boolean = true) {
        module.onBuildSuccess {
            val commandHelper = CommandHelper(module.getClasspathList(), target, module.getTestDirPath("kex-tests"))
            val runCommand = commandHelper.default()
            val cancelCommand = runCommand.containerName?.let { DockerKillCommand(it) }
            val consoleView =
                if (attachConsoleView) ToolWindowHelper(project, TITLE).newConsoleView("$TITLE Output")
                else null

            val commandBackgroundable = CommandBackgroundable(project, TITLE, runCommand, cancelCommand, consoleView)
            ProgressManager.getInstance().run(commandBackgroundable)
        }
    }
}