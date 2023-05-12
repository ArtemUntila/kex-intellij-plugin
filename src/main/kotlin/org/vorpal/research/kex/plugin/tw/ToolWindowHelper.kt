package org.vorpal.research.kex.plugin.tw

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager

class ToolWindowHelper(private val project: Project, id: String) {

    private val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(id)!!
    private val contentManager = toolWindow.contentManager
    private val consoleBuilderFactory = TextConsoleBuilderFactory.getInstance()

    fun newConsoleView(displayName: String, select: Boolean = true, show: Boolean = true): ConsoleView {
        val consoleView = consoleBuilderFactory.createBuilder(project).console
        val content = contentManager.factory.createContent(consoleView.component, displayName, true)
        contentManager.addContent(content)
        if (select) contentManager.setSelectedContent(content)
        if (show) toolWindow.show()
        return consoleView
    }
}