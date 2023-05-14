package org.vorpal.research.kex.plugin.gui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.FrameWrapper
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent


class KexWindow(
    project: Project, title: String, componentWrapper: JComponentWrapper,
    width: Int = 1024, height: Int = 768
) {

    private val frameWrapper = FrameWrapper(project = project, title = title, component = componentWrapper.component)
    private val size = Dimension(width, height)

    private companion object {
        val minimumSize = Dimension(800, 600)
    }

    init {
        val frame = frameWrapper.getFrame()
        frame.minimumSize = minimumSize
        frame.size = size
        frame.setLocationRelativeTo(null)  // center
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