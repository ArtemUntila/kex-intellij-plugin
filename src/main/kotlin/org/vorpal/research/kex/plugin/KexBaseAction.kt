package org.vorpal.research.kex.plugin

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.module.Module
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessModuleDir
import com.intellij.openapi.project.rootManager
import com.intellij.openapi.roots.OrderEnumerator
import com.intellij.openapi.wm.ToolWindowManager
import org.jetbrains.jps.model.java.JavaSourceRootType
import org.jetbrains.kotlin.idea.configuration.isGradleModule
import org.jetbrains.kotlin.idea.maven.isMavenModule
import org.jetbrains.kotlin.idea.util.findModule
import org.vorpal.research.kex.plugin.util.DockerKexOptionArgs
import org.vorpal.research.kex.plugin.util.TITLE

abstract class KexBaseAction : AnAction() {

    protected fun getTestDirPath(module: Module): String {
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

    protected fun launchKex(project: Project, dockerKexOptionArgs: DockerKexOptionArgs) {

        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(TITLE)!!
        val consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console
        val content = toolWindow.contentManager.factory.createContent(consoleView.component, "$TITLE Output", true)
        content.isCloseable = true
        toolWindow.contentManager.addContent(content)
        toolWindow.show()

        //val command = dockerKexOptionArgs.list
        val kexBackgroundTask = KexBackgroundable(project, TITLE, dockerKexOptionArgs, consoleView)

        ProgressManager.getInstance().run(kexBackgroundTask)
    }

    protected fun getModuleClasspathList(module: Module): List<String> {
        val classpathList = OrderEnumerator.orderEntries(module).recursively().pathsList.pathList
        return classpathList.filter { !it.contains("jdk") }  // TODO: Find something more efficient
    }
}