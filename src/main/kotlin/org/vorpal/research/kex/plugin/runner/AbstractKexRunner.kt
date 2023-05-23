package org.vorpal.research.kex.plugin.runner

import com.intellij.openapi.module.Module
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.task.ProjectTaskManager
import org.vorpal.research.kex.plugin.TITLE
import org.vorpal.research.kex.plugin.command.DockerKillCommand
import org.vorpal.research.kex.plugin.command.DockerRunCommand
import org.vorpal.research.kex.plugin.tw.ToolWindowHelper
import org.vorpal.research.kex.plugin.util.getClasspathList
import org.vorpal.research.kex.plugin.util.getTestDirPath

abstract class AbstractKexRunner(private val project: Project) {

    private val projectTaskManager = ProjectTaskManager.getInstance(project)
    private val toolWindowHelper = ToolWindowHelper(project, TITLE)
    private val progressManager = ProgressManager.getInstance()

    fun buildAndRun(module: Module, target: String, attachConsoleView: Boolean) {
        projectTaskManager.build(module).onSuccess { buildResult ->
            if (buildResult.isAborted || buildResult.hasErrors()) return@onSuccess

            val commandHelper = CommandHelper(module.getClasspathList(), target, module.getTestDirPath("kex-tests"))
            val runCommand = dockerRunCommand(commandHelper)
            val cancelCommand = runCommand.containerName?.let { DockerKillCommand(it) }
            val consoleView =
                if (attachConsoleView) toolWindowHelper.newConsoleView("$TITLE Output")
                else null

            val backgroundable = CommandBackgroundable(project, TITLE, runCommand, cancelCommand, consoleView)
            progressManager.run(backgroundable)

            postRun(target, backgroundable)
        }
    }

    protected abstract fun dockerRunCommand(commandHelper: CommandHelper): DockerRunCommand

    protected open fun postRun(target: String, backgroundable: CommandBackgroundable) {}
}