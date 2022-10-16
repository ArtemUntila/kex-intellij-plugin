package org.vorpal.research.kex.plugin

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.OrderEnumerator
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.task.ProjectTaskManager
import org.vorpal.research.kex.plugin.util.*
import java.io.File

class ProjectViewAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val output = chooseFolder(project) ?: return  // Avoiding Error in IDE
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        val target = getPsiFileFQN(psiFile)
        val classpathList = getClasspathList(project)

        val projectTaskManager = ProjectTaskManager.getInstance(e.project)
        // There is an approach to get module dependencies and build it separately, but it is much more complicated (it will also affect the way classpath is obtained)
        val buildResult = projectTaskManager.buildAllModules()

        buildResult.onSuccess {
            println(getDockerKexArgsList(classpathList, output, target))
            launchKex(project, classpathList, output, target)
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
        //kexBackgroundTask.command = getKEXArgsList(cp, target, output)
        kexBackgroundTask.command = getDockerKexArgsList(classpathList, output, target)
        kexBackgroundTask.directory = File(KEX_PATH)
        kexBackgroundTask.consoleView = consoleView

        ProgressManager.getInstance().run(kexBackgroundTask)
    }

    private fun getClasspath(project: Project): String {
        return getClasspathList(project).joinToString(System.getProperty("path.separator"))
    }

    private fun getClasspathList(project: Project): List<String> {
        val classpathList = OrderEnumerator.orderEntries(project).recursively().pathsList.pathList
        return classpathList.filter { !it.contains("jdk") }  // TODO: Find something more efficient
    }

    private fun chooseFolder(project: Project): String? {
        val fcd = FileChooserDescriptor(
            false,
            true,
            false,
            false,
            false,
            false
        )

        fcd.title = TITLE
        fcd.description = "Choose folder for auto-generated tests"

        var folderPath: String? = null

        FileChooser.chooseFile(fcd, project, null) {
            folderPath = it.canonicalPath.toString()
        }

        return folderPath
    }
}