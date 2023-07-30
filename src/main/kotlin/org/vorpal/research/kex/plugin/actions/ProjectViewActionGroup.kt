package org.vorpal.research.kex.plugin.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import org.jetbrains.kotlin.idea.util.sourceRoot
import org.vorpal.research.kex.plugin.psi.isJavaOrKotlinSource

class ProjectViewActionGroup : DefaultActionGroup() {

    override fun update(e: AnActionEvent) {
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        e.presentation.isEnabledAndVisible = (psiFile?.sourceRoot != null) && psiFile.isJavaOrKotlinSource
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}