package org.vorpal.research.kex.plugin

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager

class ToolWindowHelper(private val project: Project, id: String) {

    private val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(id)!!

    fun newConsoleView(displayName: String, select: Boolean = true, show: Boolean = true): ConsoleView {
        val consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console
        val content = toolWindow.contentManager.factory.createContent(consoleView.component, displayName, true)
        toolWindow.contentManager.addContent(content)
        if (select) toolWindow.contentManager.setSelectedContent(content)
        if (show) toolWindow.show()
        return consoleView
    }
}