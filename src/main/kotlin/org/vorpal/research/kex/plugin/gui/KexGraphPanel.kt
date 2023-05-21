package org.vorpal.research.kex.plugin.gui

import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Scene
import java.util.function.Consumer


class KexGraphPanel(width: Double = 1024.0, height: Double = 768.0) : JFXComponentWrapper() {

    private companion object {
        val classLoader = Companion::class.java.classLoader
        val CSS_FILE = classLoader.getResource("smartgraph/smartgraph.css")?.toURI()
        val PROPERTIES = SmartGraphProperties(classLoader.getResourceAsStream("smartgraph/smartgraph.properties"))
    }

    private val graph = KexGraph()
    private val graphView = KexSmartGraphPanel(graph, CSS_FILE, PROPERTIES)

    private val controlPane = KexControlPane(graphView)
    private val controlsDisabled = SimpleBooleanProperty(true)
    private val continueSelected = SimpleBooleanProperty(false)

    private var reverseAction: Consumer<KexVertex>? = null
    private var nextAction: Runnable? = null

    init {
        setScene(Scene(controlPane, width, height))
        graphView.setAutomaticLayout(true)
        graphView.init()
        initControls()
    }

    private fun initControls() {
        controlPane.nextButton.disableProperty().bindBidirectional(controlsDisabled)
        controlPane.continueCheckBox.selectedProperty().bindBidirectional(continueSelected)

        controlPane.nextButton.setOnAction {
            disableControls()
            nextAction?.run()
        }

        graphView.showVertexContextMenuIf {
            !controlsDisabled.value && (it.underlyingVertex.element().type == KexVertexType.PATH)
        }

        graphView.addContextMenuItem("reverse") { smartGraphVertex ->
            disableControls()
            reverseAction?.accept(smartGraphVertex.underlyingVertex.element())
        }
    }

    fun onPathVertexReverse(consumer: Consumer<KexVertex>) {
        reverseAction = consumer
    }

    fun onNext(runnable: Runnable) {
        nextAction = runnable
    }

    fun addVerticesAsTrace(vertices: Iterable<KexVertex>) {
        var updated = false

        var prevVertex: KexVertex? = null
        for (currentVertex in vertices) {
            updated = graph.addVertex(currentVertex) || updated

            prevVertex?.let {
                updated = graph.addEdge(it, currentVertex) || updated
            }

            prevVertex = currentVertex
        }

        if (updated) update()

        if (continueSelected.value) nextAction?.run()
        else enableControls()
    }

    private fun disableControls() {
        controlsDisabled.value = true
    }

    private fun enableControls() {
        controlsDisabled.value = false
    }

    fun stopControls() {
        disableControls()
        controlPane.continueCheckBox.isDisable = true
    }

    fun update(wait: Boolean = false) {
        if (!wait) graphView.update()
        else graphView.updateAndWait()
    }
}