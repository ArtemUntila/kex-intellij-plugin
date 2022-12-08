package org.vorpal.research.kex.plugin

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.ui.Messages
import com.intellij.task.ProjectTaskManager
import org.vorpal.research.kex.plugin.util.DockerKexOptionArgs
import org.vorpal.research.kex.plugin.util.fileClassFqName

class ProjectViewAction : KexBaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        val module = ModuleUtil.findModuleForFile(psiFile) ?: return

        val target = psiFile.fileClassFqName?.asString() ?: return
        val classpathList = getModuleClasspathList(module)

        val projectTaskManager = ProjectTaskManager.getInstance(project)
        val buildResult = projectTaskManager.build(module)

        val testDirPath = getTestDirPath(module)
        val dockerKexOptionArgs = DockerKexOptionArgs(classpathList, target, testDirPath)

        buildResult.onSuccess {
            Messages.showInfoMessage(project, psiFile::class.qualifiedName, "Class Qualified Name")
            Messages.showInfoMessage(project, target, "Pdi File Target Name")
            Messages.showInfoMessage(project, testDirPath, "Test Dir")
            Messages.showInfoMessage(project, "${dockerKexOptionArgs.list}", "Docker Kex Option Args")
            launchKex(project, dockerKexOptionArgs)
        }
    }
}