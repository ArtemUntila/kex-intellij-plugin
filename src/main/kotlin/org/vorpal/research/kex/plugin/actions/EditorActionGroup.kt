package org.vorpal.research.kex.plugin.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import org.vorpal.research.kex.plugin.psi.isIdentifier
import org.vorpal.research.kex.plugin.psi.isJavaOrKotlinClass
import org.vorpal.research.kex.plugin.psi.isJavaOrKotlinMethod

class EditorActionGroup : DefaultActionGroup() {

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.PSI_FILE) ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return

        val element = file.viewProvider.findElementAt(editor.caretModel.offset) ?: return
        val parent = element.parent ?: return

        e.presentation.isEnabledAndVisible = element.isIdentifier &&
                (parent.isJavaOrKotlinClass || parent.isJavaOrKotlinMethod)
    }
}