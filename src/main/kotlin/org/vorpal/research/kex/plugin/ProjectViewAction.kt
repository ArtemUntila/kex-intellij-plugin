package org.vorpal.research.kex.plugin

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessModuleDir
import com.intellij.openapi.project.rootManager
import com.intellij.openapi.roots.OrderEnumerator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.task.ProjectTaskManager
import org.jetbrains.jps.model.java.JavaSourceRootType
import org.jetbrains.kotlin.fileClasses.fileClassInfo
import org.jetbrains.kotlin.idea.configuration.isGradleModule
import org.jetbrains.kotlin.idea.maven.isMavenModule
import org.jetbrains.kotlin.idea.util.findModule
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.vorpal.research.kex.plugin.util.*

class ProjectViewAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return

        // TODO: insert into psiUtils
        if (psiFile is KtFile) {
            Messages.showInfoMessage(project, psiFile.fileClassInfo.fileClassFqName.asString(), "File Class Fq Name")
        }
        //println("JavaClass: ${psiFile is }")
        val module = ModuleUtil.findModuleForFile(psiFile) ?: return

        val target = getPsiFileFQN(psiFile)
        val classpathList = getClasspathList(project)

        val projectTaskManager = ProjectTaskManager.getInstance(project)
        val buildResult = projectTaskManager.build(module)

        //val buildSystem = ExternalSystemModulePropertyManager.getInstance(module).getExternalSystemId()?.uppercase()
        //val moduleVirtualFile = module.guessModuleDir()
        //val modulePath = moduleVirtualFile?.canonicalPath
        //val sourceRoots = module.sourceRoots.map { it.path }
        val testDirPath = getTestDirPath(module)
        val dockerKexOptionArgs = DockerKexOptionArgs(classpathList, target, testDirPath)

        val dk = DataKey.allKeys().map { it.name }.filter { it.contains("psi") }

        Messages.showInfoMessage(e.project, dk.joinToString("\n"), "DataKeys")

//        buildResult.onSuccess {
//            Messages.showInfoMessage(project, testDirPath, "Test Dir")
//            Messages.showInfoMessage(project, "${dockerKexOptionArgs.list}", "Docker Kex Option Args")
//            launchKex(project, dockerKexOptionArgs)
//        }
    }

    private fun getTestDirPath(module: Module): String {
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

    private fun launchKex(project: Project, dockerKexOptionArgs: DockerKexOptionArgs) {

        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(TITLE)!!
        val consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console
        val content = toolWindow.contentManager.factory.createContent(consoleView.component, "$TITLE Output", true)
        content.isCloseable = true
        toolWindow.contentManager.addContent(content)
        toolWindow.show()

        val kexBackgroundTask = KexBackgroundable(project, TITLE)
        kexBackgroundTask.command = dockerKexOptionArgs.list
        kexBackgroundTask.consoleView = consoleView

        ProgressManager.getInstance().run(kexBackgroundTask)
    }

    private fun getClasspathList(project: Project): List<String> {
        val classpathList = OrderEnumerator.orderEntries(project).recursively().pathsList.pathList
        return classpathList.filter { !it.contains("jdk") }  // TODO: Find something more efficient
    }
}