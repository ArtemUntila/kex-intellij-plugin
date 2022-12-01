package org.vorpal.research.kex.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.base.utils.fqname.getKotlinFqName
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtNamedFunction

class EditorAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val psiElement = EditorActionGroup.psiElement
        Messages.showInfoMessage(e.project, psiElement::class.qualifiedName, "Class Qualified Name")
        Messages.showInfoMessage(e.project, psiElement.getKotlinFqName()?.asString(), "PsiElement KotlinFqName")
        //Messages.showInfoMessage(e.project, psiElement.originalElement::class.qualifiedName, "Psi Original Element")
        Messages.showInfoMessage(e.project, psiElement.text, "PsiElement Text")
        Messages.showInfoMessage(e.project, psiElement.containingFile.toString(), "Containing File")
        Messages.showInfoMessage(e.project, psiElement.parent.toString(), "Parent")
        Messages.showInfoMessage(e.project, psiElement.parent::class.qualifiedName, "Parent Class")
    }
}