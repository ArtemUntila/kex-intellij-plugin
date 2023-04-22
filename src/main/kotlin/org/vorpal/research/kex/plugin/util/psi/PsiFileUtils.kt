package org.vorpal.research.kex.plugin.util.psi

import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import org.jetbrains.kotlin.fileClasses.javaFileFacadeFqName
import org.jetbrains.kotlin.idea.KotlinIconProviderBase
import org.jetbrains.kotlin.idea.core.getFqNameByDirectory
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtFile

val PsiFile.fileClassFqName: FqName?
    get() = when (this) {
            is KtFile -> this.fileClassFqName
            is PsiJavaFile -> this.fileClassFqName
            else -> null
    }

val PsiJavaFile.fileClassFqName: FqName
    get() = this.fileClassFqName(".java")

val KtFile.fileClassFqName: FqName
    get() =
        if (this.isSingleClassFile) this.fileClassFqName(".kt")
        else this.javaFileFacadeFqName

val KtFile.isSingleClassFile: Boolean
    get() = KotlinIconProviderBase.isSingleClassFile(this)

private fun PsiFile.fileClassFqName(extensionSuffix: String): FqName {
    val className = this.name.removeSuffix(extensionSuffix)
    val packageName = this.getFqNameByDirectory().asString()
    return if (packageName.isEmpty()) FqName(className)
    else FqName("$packageName.$className")
}