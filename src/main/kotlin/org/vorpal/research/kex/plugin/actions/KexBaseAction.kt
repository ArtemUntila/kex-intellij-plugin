package org.vorpal.research.kex.plugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessModuleDir
import com.intellij.openapi.project.rootManager
import com.intellij.openapi.roots.OrderEnumerator
import com.intellij.task.ProjectTaskManager
import org.jetbrains.jps.model.java.JavaSourceRootType
import org.jetbrains.kotlin.idea.configuration.isGradleModule
import org.jetbrains.kotlin.idea.maven.isMavenModule
import org.jetbrains.kotlin.idea.util.findModule
import org.vorpal.research.kex.plugin.KexTaskManager
import org.vorpal.research.kex.plugin.command.dockerRunKexCommand

abstract class KexBaseAction : AnAction() {

    protected fun launchKex(project: Project, module: Module, target: String) {
        val projectTaskManager = ProjectTaskManager.getInstance(project)
        val buildResult = projectTaskManager.build(module)

        buildResult.onSuccess {
            if (it.hasErrors()) return@onSuccess

            val classpathList = getModuleClasspathList(module)
            val testDirPath = getModuleTestDirPath(module)

            val dockerRunKexCommand = dockerRunKexCommand(classpathList, target, testDirPath)

            val kexTaskManager = KexTaskManager(project)
            kexTaskManager.showConsole()
            kexTaskManager.run(dockerRunKexCommand)
        }
    }

    private fun getModuleTestDirPath(module: Module): String {
        val moduleVirtualFile = module.guessModuleDir()!!

        val testDirPath = if (module.isMavenModule()) {
            module.rootManager.getSourceRoots(JavaSourceRootType.TEST_SOURCE).first().path
        } else if (module.isGradleModule()) {
            val testVirtualFile = moduleVirtualFile.parent.children.find { it.name == "test" }!!
            val testModule = testVirtualFile.findModule(module.project)!!
            testModule.rootManager.getSourceRoots(JavaSourceRootType.TEST_SOURCE).first().path
        } else {
            "${moduleVirtualFile.path}/kex-tests"
        }

        return testDirPath
    }

    private fun getModuleClasspathList(module: Module): List<String> {
        val classpathList = OrderEnumerator.orderEntries(module).recursively().pathsList.pathList
        return classpathList.filter { !it.contains("jdk") }  // TODO: Find something more efficient
    }
}