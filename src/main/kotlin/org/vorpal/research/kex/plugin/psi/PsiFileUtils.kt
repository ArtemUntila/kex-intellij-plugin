package org.vorpal.research.kex.plugin.psi

import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import org.jetbrains.kotlin.fileClasses.javaFileFacadeFqName
import org.jetbrains.kotlin.idea.KotlinIconProvider
import org.jetbrains.kotlin.idea.core.getFqNameByDirectory
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtFile

val PsiFile.fileClassFqName: FqName?
    get() = when (this) {
        is KtFile -> fileClassFqName
        is PsiJavaFile -> fileClassFqName
        else -> null
    }

val PsiJavaFile.fileClassFqName: FqName
    get() = fileClassFqName(".java")

val KtFile.fileClassFqName: FqName
    get() =
        if (isSingleClassFile) fileClassFqName(".kt")
        else javaFileFacadeFqName

val KtFile.isSingleClassFile: Boolean
    get() = KotlinIconProvider.isSingleClassFile(this)

private fun PsiFile.fileClassFqName(extensionSuffix: String): FqName {
    val className = name.removeSuffix(extensionSuffix)
    val packageName = getFqNameByDirectory().asString()

    return if (packageName.isEmpty()) FqName(className)
    else FqName("$packageName.$className")
}