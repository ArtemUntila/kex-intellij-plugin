package org.vorpal.research.kex.plugin.actions.editor

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import org.vorpal.research.kex.plugin.psi.targetName
import org.vorpal.research.kex.plugin.runner.AbstractKexRunner
import org.vorpal.research.kex.plugin.runner.KexRunner

open class EditorAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT) ?: return
        val module = ModuleUtil.findModuleForPsiElement(psiElement) ?: return
        val target = psiElement.targetName ?: return

        kexRunner(project).buildAndRun(module, target, true)
    }

    protected open fun kexRunner(project: Project): AbstractKexRunner = KexRunner(project)
}