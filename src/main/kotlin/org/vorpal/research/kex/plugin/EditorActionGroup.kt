package org.vorpal.research.kex.plugin

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFunction

class EditorActionGroup : DefaultActionGroup() {

    companion object {
        lateinit var psiElement: PsiElement
    }

    override fun update(e: AnActionEvent) {
//        val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT)
//        val isJavaElement = psiElement is PsiClass || psiElement is PsiMethod
//        val isKotlinElement = psiElement is KtClass || (psiElement is KtNamedFunction && psiElement.funKeyword != null)
//        val isClass = psiElement is PsiClass || psiElement is KtClass
//        e.presentation.isEnabledAndVisible = (psiElement != null)// && isClass//(isJavaElement || isKotlinElement)

        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val psiElement = psiFile.viewProvider.findElementAt(editor.caretModel.offset)?.parent
        if (psiElement != null) {
            val isJavaElement = psiElement is PsiClass || psiElement is PsiMethod
            val isKotlinElement = psiElement is KtClass || psiElement is KtFunction
            e.presentation.isEnabledAndVisible = isJavaElement || isKotlinElement
            EditorActionGroup.psiElement = psiElement
        }
    }
}