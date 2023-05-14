package org.vorpal.research.kex.plugin.gui

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties
import javafx.scene.Scene


class KexGraphPanel : JFXComponentWrapper() {

    private companion object {
        val classLoader = Companion::class.java.classLoader
        val CSS_FILE = classLoader.getResource("smartgraph/smartgraph.css")?.toURI()
        val PROPERTIES = SmartGraphProperties(classLoader.getResourceAsStream("smartgraph/smartgraph.properties"))
    }

    private val graph = KexGraph()
    private val graphView = SmartGraphPanel(graph, PROPERTIES, null, CSS_FILE)
    private val graphPane = SmartGraphDemoContainer(graphView)

    init {
        setScene(Scene(graphPane))
        graphView.setAutomaticLayout(true)
        graphView.init()
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
    }

    fun update(wait: Boolean = false) {
        if (!wait) graphView.update()
        else graphView.updateAndWait()
    }
}