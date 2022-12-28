package org.vorpal.research.kex.plugin

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.module.ModuleUtil
import org.vorpal.research.kex.plugin.util.psi.fileClassFqName

class ProjectViewAction : KexBaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        val module = ModuleUtil.findModuleForFile(psiFile) ?: return
        val target = psiFile.fileClassFqName?.asString() ?: return

        launchKex(project, module, target)
    }
}