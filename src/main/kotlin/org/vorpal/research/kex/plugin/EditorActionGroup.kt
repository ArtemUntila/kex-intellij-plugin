package org.vorpal.research.kex.plugin

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.elementType
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFunction

class EditorActionGroup : DefaultActionGroup() {

    override fun update(e: AnActionEvent) {
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return

        val psiElement = psiFile.viewProvider.findElementAt(editor.caretModel.offset)
        val psiElementParent = psiElement?.parent

        if (psiElement == null || psiElementParent == null) return

        // Couldn't come up with something more elegant
        val isIdentifierElement = psiElement.elementType?.debugName == "IDENTIFIER"
        val isJavaParent = psiElementParent is PsiClass || psiElementParent is PsiMethod
        val isKotlinParent = psiElementParent is KtClass || psiElementParent is KtFunction

        e.presentation.isEnabledAndVisible = isIdentifierElement && (isJavaParent || isKotlinParent)
    }
}