package org.vorpal.research.kex.plugin.util

import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import org.jetbrains.kotlin.psi.KtFile

fun getPsiFileFQN(psiFile: PsiFile): String {
    val className = getClassName(psiFile.name)

    // TODO: replace with psiFile.getFqNameByDirectory().asString()
    var packageName = ""
    when (psiFile) {
        is PsiJavaFile -> packageName = psiFile.packageName
        is KtFile -> packageName = psiFile.packageName
    }

    return if (packageName.isEmpty()) className
    else "$packageName.${className}"
}

// TODO: find a way to get FQN name for Kotlin file ("main.kt" -> "MainKt")
fun getClassName(fileName: String): String {
    return fileName.removeSuffix(".java").removeSuffix(".kt")
}