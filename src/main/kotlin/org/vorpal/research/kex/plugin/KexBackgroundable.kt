package org.vorpal.research.kex.plugin

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import kotlin.concurrent.thread

class KexBackgroundable(project: Project, title: String,
                        private val command: List<String>,
                        private val consoleView: ConsoleView) : Backgroundable(project, title) {

//    var command: List<String>? = null
//    var consoleView: ConsoleView? = null

    override fun run(indicator: ProgressIndicator) {
        listenCanceled(indicator)

        val processHandler = OSProcessHandler(GeneralCommandLine(command))
        processHandler.startNotify()
        consoleView.attachToProcess(processHandler)
        processHandler.waitFor()
    }

    private fun listenCanceled(indicator: ProgressIndicator) {
        // Checks if "Cancel" button has been pressed (once a second)
        thread(true) {
            while (indicator.isRunning) {
                try {
                    indicator.checkCanceled()
                    println("checkCanceled")
                    Thread.sleep(1000)
                } catch (pce: ProcessCanceledException) {
                    ProcessBuilder("docker", "kill", "123").start().waitFor()
                }
            }
            println("Thread finished")
        }
    }

    override fun onCancel() {
        println("Cancel")
        super.onCancel()
    }

}