package org.vorpal.research.kex.plugin

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import org.vorpal.research.kex.plugin.command.DockerRunCommand

class KexTaskManager(private val project: Project) {

    private val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(TITLE)!!
    private val consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console

    fun showConsole() {
        val content = toolWindow.contentManager.factory.createContent(consoleView.component, "$TITLE Output", true)
        content.isCloseable = true
        toolWindow.contentManager.addContent(content)
        toolWindow.contentManager.setSelectedContent(content)
        toolWindow.show()
    }

    fun run(dockerRunCommand: DockerRunCommand) {
        val kexBackgroundTask = KexBackgroundable(project, TITLE, dockerRunCommand, consoleView)
        ProgressManager.getInstance().run(kexBackgroundTask)
    }
}