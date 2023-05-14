package org.vorpal.research.kex.plugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.module.ModuleUtil
import org.vorpal.research.kex.plugin.runner.KexRunner

import org.vorpal.research.kex.plugin.psi.targetName

class EditorAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT) ?: return
        val module = ModuleUtil.findModuleForPsiElement(psiElement) ?: return
        val target = psiElement.targetName ?: return

        KexRunner(module).buildAndRun(target, true)
    }
}