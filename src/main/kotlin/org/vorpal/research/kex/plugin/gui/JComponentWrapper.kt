package org.vorpal.research.kex.plugin.gui

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import java.awt.Dimension
import javax.swing.JComponent


interface JComponentWrapper {
    val component: JComponent
    val size: Dimension
}

open class JFXComponentWrapper : JComponentWrapper {
    // JFXPanel constructor initializes JavaFX Runtime
    private val jfxPanel = JFXPanel()

    override val component: JComponent
        get() = jfxPanel

    override val size: Dimension
        get() = jfxPanel.preferredSize

    init {
        // Don't shut down JavaFX Runtime when the last window is closed
        Platform.setImplicitExit(false)
    }

    protected fun setScene(scene: Scene) {
        jfxPanel.scene = scene
    }
}