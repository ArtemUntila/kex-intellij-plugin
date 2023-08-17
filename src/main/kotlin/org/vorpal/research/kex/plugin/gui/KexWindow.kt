package org.vorpal.research.kex.plugin.gui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.FrameWrapper
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent


class KexWindow(project: Project, title: String, content: JComponentWrapper) {

    private companion object {
        val minimumSize = Dimension(800, 600)
    }

    private val frameWrapper = FrameWrapper(project = project, title = title, component = content.component)

    init {
        with(frameWrapper.getFrame()) {
            minimumSize = Companion.minimumSize
            size = content.size
            setLocationRelativeTo(null)  // center
        }
        frameWrapper.show(false)
    }

    fun onClosed(block: () -> Unit) {
        frameWrapper.getFrame().addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                block()
            }
        })
    }
}