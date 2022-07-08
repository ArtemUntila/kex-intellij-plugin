package org.vorpal.research.kex.plugin

import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import java.io.File

class KexBackgroundable(project: Project, title: String) : Backgroundable(project, title) {

    var command: List<String>? = null
    var directory: File? = null
    var consoleView: ConsoleView? = null

    override fun run(indicator: ProgressIndicator) {
        val processBuilder = ProcessBuilder(command)  // Separate Const/companion?
        processBuilder.directory(directory)
        val process = processBuilder.start()
        val processHandler = OSProcessHandler(process, null)
        processHandler.startNotify()
        try {
            consoleView?.attachToProcess(processHandler)
            process.waitFor()
        } catch (npe: NullPointerException) {
            process.destroy()
        }
    }

    // TODO: Destroy Process (process.destroy doesn't seem to work)
    override fun onCancel() {
        super.onCancel()
    }

}