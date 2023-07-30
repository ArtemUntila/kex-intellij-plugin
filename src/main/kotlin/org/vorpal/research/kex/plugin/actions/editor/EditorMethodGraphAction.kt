package org.vorpal.research.kex.plugin.actions.editor

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import org.vorpal.research.kex.plugin.psi.isJavaOrKotlinMethod
import org.vorpal.research.kex.plugin.runner.AbstractKexRunner
import org.vorpal.research.kex.plugin.runner.KexGUIRunner

// Inheritance is evil, but code duplication hurts the eye more
class EditorMethodGraphAction : EditorAction() {

    override fun kexRunner(project: Project): AbstractKexRunner = KexGUIRunner(project)

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.getData(CommonDataKeys.PSI_ELEMENT)?.isJavaOrKotlinMethod == true
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}