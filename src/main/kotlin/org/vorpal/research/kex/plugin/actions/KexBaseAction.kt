package org.vorpal.research.kex.plugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.task.ProjectTaskManager
import org.vorpal.research.kex.plugin.KexTaskManager
import org.vorpal.research.kex.plugin.command.dockerRunKexCommand
import org.vorpal.research.kex.plugin.util.getClasspathList
import org.vorpal.research.kex.plugin.util.getTestDirPath

abstract class KexBaseAction : AnAction() {

    protected fun launchKex(project: Project, module: Module, target: String) {
        val projectTaskManager = ProjectTaskManager.getInstance(project)
        val buildResult = projectTaskManager.build(module)

        buildResult.onSuccess {
            if (it.hasErrors() || it.isAborted) return@onSuccess

            val dockerRunKexCommand = dockerRunKexCommand(module.getClasspathList(), target, module.getTestDirPath())

            val kexTaskManager = KexTaskManager(project)
            kexTaskManager.showConsole()
            kexTaskManager.run(dockerRunKexCommand)
        }
    }
}