package org.vorpal.research.kex.plugin

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup

class ProjectViewActionGroup : DefaultActionGroup() {

    override fun update(e: AnActionEvent) {
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val isEnabledAndVisible = (e.project != null) && (virtualFile != null) &&
                (virtualFile.name.endsWith(".java") || virtualFile.name.endsWith(".kt"))
        e.presentation.isEnabledAndVisible = isEnabledAndVisible
    }
}