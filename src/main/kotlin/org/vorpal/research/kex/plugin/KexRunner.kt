package org.vorpal.research.kex.plugin

import com.intellij.openapi.module.Module
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import org.vorpal.research.kex.plugin.command.dockerRunKexCommand
import org.vorpal.research.kex.plugin.tw.ToolWindowHelper
import org.vorpal.research.kex.plugin.util.getClasspathList
import org.vorpal.research.kex.plugin.util.getTestDirPath
import org.vorpal.research.kex.plugin.util.onBuildSuccess

class KexRunner(private val project: Project) {

    fun run(module: Module, target: String, attachConsoleView: Boolean = true) {
        module.onBuildSuccess {
            val dockerRunKexCommand = dockerRunKexCommand(
                module.getClasspathList(), target, module.getTestDirPath("kex-tests")
            )

            val consoleView =
                if (attachConsoleView) ToolWindowHelper(project, TITLE).newConsoleView("$TITLE Output")
                else null

            val dockerRunBackgroundable = DockerRunBackgroundable(project, TITLE, dockerRunKexCommand, consoleView)
            ProgressManager.getInstance().run(dockerRunBackgroundable)
        }
    }
}