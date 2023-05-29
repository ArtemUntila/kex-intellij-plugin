package org.vorpal.research.kex.plugin.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup

class ProjectViewActionGroup : DefaultActionGroup() {

    override fun update(e: AnActionEvent) {
        val fileName = e.getData(CommonDataKeys.VIRTUAL_FILE)?.name ?: return
        e.presentation.isEnabledAndVisible = (fileName.endsWith(".java") || fileName.endsWith(".kt"))
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}