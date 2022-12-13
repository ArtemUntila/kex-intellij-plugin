package org.vorpal.research.kex.plugin.util

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.idea.base.utils.fqname.fqName
import org.jetbrains.kotlin.idea.util.toJvmFqName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.nj2k.postProcessing.type
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.psiUtil.containingClass

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

val PsiMethod.targetName: String
    get() {
        val className = containingClass?.fqName?.toJvmFqName
        val returnTypeName = returnType?.canonicalText
        val parameterTypeNames = parameterList.parameters.joinToString(",") { it.type.canonicalText }
        return formatFunTargetName(className!!, name, returnTypeName!!, parameterTypeNames)
    }

val KtFunction.targetName: String
    get() {
        val parentName = containingClass()?.fqName?.toJvmFqName ?: containingFile.fileClassFqName?.asString()
        val returnTypeName = type()?.fqName?.asString()
        val parameterTypeNames = valueParameters.map { it.type()?.fqName?.asString() }.joinToString(",")
        return formatFunTargetName(parentName!!, name!!, returnTypeName!!, parameterTypeNames)
    }

private fun formatFunTargetName(
    className: String,
    funName: String,
    returnTypeName: String,
    parameterTypeNames: String
): String = "$className::$funName($parameterTypeNames):$returnTypeName"