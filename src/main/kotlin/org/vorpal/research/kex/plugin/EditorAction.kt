package org.vorpal.research.kex.plugin

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.module.ModuleUtil
import org.vorpal.research.kex.plugin.util.psi.targetName

class EditorAction : KexBaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT) ?: return
        val module = ModuleUtil.findModuleForPsiElement(psiElement) ?: return
        val target = psiElement.targetName ?: return

        launchKex(project, module, target)
    }
}