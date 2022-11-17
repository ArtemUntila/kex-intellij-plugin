package org.vorpal.research.kex.plugin

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.externalSystem.ExternalSystemModulePropertyManager
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
import org.jetbrains.kotlin.idea.configuration.isGradleModule
import org.jetbrains.kotlin.idea.maven.isMavenModule
import org.jetbrains.kotlin.idea.util.findModule
import org.jetbrains.kotlin.idea.util.projectStructure.getModule
import org.jetbrains.kotlin.idea.util.sourceRoots
import org.vorpal.research.kex.plugin.util.*

class ProjectViewAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

//        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
//        val target = getPsiFileFQN(psiFile)
//        val classpathList = getClasspathList(project)
//
//        val projectTaskManager = ProjectTaskManager.getInstance(project)
//        // There is an approach to get module dependencies and build it separately, but it is much more complicated (it will also affect the way classpath is obtained)
//        val buildResult = projectTaskManager.buildAllModules()
//        val modules = project.allModules()
//        val additionalOptions = CompilerConfiguration.getInstance(project).getAdditionalOptions(modules.first())

//        buildResult.onSuccess {
//            val dockerKexArgsList = getDockerKexArgsList(classpathList, "", target)
//            Messages.showInfoMessage(project, dockerKexArgsList.joinToString("\n"), "Command")
//            Messages.showInfoMessage(project, modules.joinToString("\n"), "All Modules")
//            //launchKex(project, classpathList, "", target)
//        }

        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        val module = ModuleUtil.findModuleForFile(psiFile) ?: return

        val projectTaskManager = ProjectTaskManager.getInstance(project)
        val buildResult = projectTaskManager.build(module)

        val buildSystem = ExternalSystemModulePropertyManager.getInstance(module).getExternalSystemId()?.uppercase()
        val moduleVirtualFile = module.guessModuleDir()
        val modulePath = moduleVirtualFile?.canonicalPath
        val sourceRoots = module.sourceRoots.map { it.path }
        var testRoots = module.rootManager.getSourceRoots(JavaSourceRootType.TEST_SOURCE).joinToString("\n") { it.path }

        if (module.isGradleModule()) {
            val testVirtualFile = moduleVirtualFile?.parent?.children?.find { it.name == "test" }
            val testModule = testVirtualFile!!.findModule(project)!!
            testRoots = testModule.rootManager.getSourceRoots(JavaSourceRootType.TEST_SOURCE).joinToString("\n") { it.path }
        }

        buildResult.onSuccess {
            Messages.showInfoMessage(project, "Module [${module.name}] is successfully built", "Build Result")
            Messages.showInfoMessage(project, buildSystem, "External System Id")
            Messages.showInfoMessage(project, "${module.isMavenModule()}", "Is Maven?")
            Messages.showInfoMessage(project, "${module.isGradleModule()}", "Is Gradle?")
            Messages.showInfoMessage(project, modulePath, "Module Dir Path")
            Messages.showInfoMessage(project, sourceRoots.joinToString("\n"), "Module Source Roots")
            Messages.showInfoMessage(project, testRoots, "Module Test Roots")
        }
    }

    private fun launchKex(project: Project, classpathList: List<String>, output:String, target: String) {

        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(TITLE)!!
        val consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console
        val content = toolWindow.contentManager.factory.createContent(consoleView.component, "$TITLE Output", true)
        content.isCloseable = true
        toolWindow.contentManager.addContent(content)
        toolWindow.show()

        val kexBackgroundTask = KexBackgroundable(project, TITLE)
        kexBackgroundTask.command = getDockerKexArgsList(classpathList, output, target)
        kexBackgroundTask.consoleView = consoleView

        ProgressManager.getInstance().run(kexBackgroundTask)
    }

    private fun getClasspathList(project: Project): List<String> {
        val classpathList = OrderEnumerator.orderEntries(project).recursively().pathsList.pathList
        return classpathList.filter { !it.contains("jdk") }  // TODO: Find something more efficient
    }
}