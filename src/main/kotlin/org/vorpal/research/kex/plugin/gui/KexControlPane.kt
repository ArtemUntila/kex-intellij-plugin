package org.vorpal.research.kex.plugin.gui

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox


class KexControlPane(graphView: SmartGraphPanel<*, *>) : BorderPane() {

    val continueCheckBox = CheckBox("Continue")
    val nextButton = Button("Next")

    init {
        center = graphView

        val automatic = CheckBox("Automatic layout").apply {
            selectedProperty().bindBidirectional(graphView.automaticLayoutProperty())
        }

        bottom = HBox(10.0, automatic, continueCheckBox, nextButton).apply {
            padding = Insets(3.0)
            alignment = Pos.CENTER_LEFT
        }
    }
}