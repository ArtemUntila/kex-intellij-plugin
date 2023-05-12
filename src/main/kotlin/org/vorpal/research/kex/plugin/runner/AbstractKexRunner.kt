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

abstract class AbstractKexRunner(private val _module: Module) {

    private val _project = _module.project

    val module: Module
        get() = _module

    val project: Project
        get() = _project

    fun buildAndRun(target: String, attachConsoleView: Boolean) {
        ProjectTaskManager.getInstance(_project).build(_module).onSuccess { buildResult ->
            if (buildResult.isAborted || buildResult.hasErrors()) return@onSuccess

            val commandHelper = CommandHelper(_module.getClasspathList(), target, _module.getTestDirPath("kex-tests"))
            val runCommand = dockerRunCommand(commandHelper)
            val cancelCommand = runCommand.containerName?.let { DockerKillCommand(it) }
            val consoleView =
                if (attachConsoleView) ToolWindowHelper(_project, TITLE).newConsoleView("$TITLE Output")
                else null

            val backgroundable = CommandBackgroundable(_project, TITLE, runCommand, cancelCommand, consoleView)
            ProgressManager.getInstance().run(backgroundable)

            postRun(target, backgroundable)
        }
    }

    protected abstract fun dockerRunCommand(commandHelper: CommandHelper): DockerRunCommand

    protected open fun postRun(target: String, backgroundable: CommandBackgroundable) { }
}