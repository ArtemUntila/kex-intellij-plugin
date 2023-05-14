package org.vorpal.research.kex.plugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.module.ModuleUtil
import org.vorpal.research.kex.plugin.psi.isJavaOrKotlinMethod
import org.vorpal.research.kex.plugin.psi.targetName
import org.vorpal.research.kex.plugin.runner.KexGUIRunner

class EditorMethodGraphAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT) ?: return
        val module = ModuleUtil.findModuleForPsiElement(psiElement) ?: return
        val target = psiElement.targetName ?: return

        KexGUIRunner(project).buildAndRun(module, target, true)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.getData(CommonDataKeys.PSI_ELEMENT)?.isJavaOrKotlinMethod == true
    }
}