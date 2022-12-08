package org.vorpal.research.kex.plugin.util

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.idea.util.toJvmFqName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.psiUtil.containingClass

val PsiElement.targetName: String?
    get() = when (this) {
        is PsiClass -> this.fqName?.toJvmFqName
        is KtClass -> this.fqName?.toJvmFqName
        is PsiMethod -> this.targetName
        is KtFunction -> this.targetName
        else -> null
    }

val PsiClass.fqName: FqName?
    get() = this.qualifiedName?.let { FqName(it) }

val PsiMethod.targetName: String
    get() {
        val parentName = this.containingClass?.fqName?.toJvmFqName
        return "$parentName:${this.name}"
    }

val KtFunction.targetName: String
    get() {
        val parentName =
            this.containingClass()?.fqName?.toJvmFqName ?:
            this.containingFile.fileClassFqName?.asString()
        return "$parentName:${this.name}"
    }