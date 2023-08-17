package org.vorpal.research.kex.plugin.actions.projectView

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import org.vorpal.research.kex.plugin.psi.isJavaOrKotlinSource

class ProjectViewActionGroup : DefaultActionGroup() {

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.getData(CommonDataKeys.PSI_FILE)?.isJavaOrKotlinSource == true
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}