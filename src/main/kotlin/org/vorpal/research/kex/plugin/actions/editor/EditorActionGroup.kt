package org.vorpal.research.kex.plugin.actions.editor

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import org.vorpal.research.kex.plugin.psi.isIdentifier
import org.vorpal.research.kex.plugin.psi.isJavaOrKotlinClass
import org.vorpal.research.kex.plugin.psi.isJavaOrKotlinMethod
import org.vorpal.research.kex.plugin.psi.isJavaOrKotlinSource

class EditorActionGroup : DefaultActionGroup() {

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = false

        val file = e.getData(CommonDataKeys.PSI_FILE)
        if (file == null || !file.isJavaOrKotlinSource) return

        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val element = file.viewProvider.findElementAt(editor.caretModel.offset) ?: return
        val parent = element.parent ?: return

        e.presentation.isEnabledAndVisible = element.isIdentifier &&
                (parent.isJavaOrKotlinClass || parent.isJavaOrKotlinMethod)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}