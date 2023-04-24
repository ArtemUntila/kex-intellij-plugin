package org.vorpal.research.kex.plugin.psi

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiType
import com.intellij.psi.util.elementType
import org.jetbrains.kotlin.idea.util.toJvmFqName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.uast.getAsJavaPsiElement
import org.jetbrains.uast.toUElement

val PsiElement.targetName: String?
    get() = when (this) {
        is PsiClass -> fqName?.toJvmFqName
        is KtClass -> fqName?.toJvmFqName
        is PsiMethod -> targetName
        is KtFunction -> targetName
        else -> null
    }

val PsiClass.fqName: FqName?
    get() = qualifiedName?.let { FqName(it) }

val PsiType.fqName: FqName
    get() = FqName(canonicalText)

val PsiMethod.targetName: String?
    get() {
        val className = containingClass?.fqName?.toJvmFqName ?: return null
        val returnTypeName = returnType?.fqName?.toJvmFqName ?: return null
        val parameterTypeNames = parameterList.parameters.joinToString(",") { it.type.fqName.toJvmFqName }
        return formatFunTargetName(className, name, returnTypeName, parameterTypeNames)
    }

val KtFunction.targetName: String?
    get() {
        // Kt Element -> UAST Element -> Java Psi Element
        val psiMethod = toUElement().getAsJavaPsiElement(PsiMethod::class.java)
        return psiMethod?.targetName
    }

private fun formatFunTargetName(
    className: String,
    funName: String,
    returnTypeName: String,
    parameterTypeNames: String
): String = "$className::$funName($parameterTypeNames):$returnTypeName"

// Couldn't come up with something more elegant (working both for java and kotlin)
val PsiElement.isIdentifier: Boolean
    get() = elementType.toString() == "IDENTIFIER"

val PsiElement.isJavaOrKotlinClass: Boolean
    get() = this is PsiClass || this is KtClass

val PsiElement.isJavaOrKotlinMethod: Boolean
    get() = this is PsiMethod || this is KtFunction