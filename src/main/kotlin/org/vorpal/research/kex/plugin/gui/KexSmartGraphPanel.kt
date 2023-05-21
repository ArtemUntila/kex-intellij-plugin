package org.vorpal.research.kex.plugin.gui

import com.brunomnsilva.smartgraph.graph.Graph
import com.brunomnsilva.smartgraph.graphview.*
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import java.net.URI
import java.util.function.Consumer
import java.util.function.Predicate


class KexSmartGraphPanel<V, E>(
    graph: Graph<V, E>,
    cssFile: URI? = null,
    properties: SmartGraphProperties? = null,
    placementStrategy: SmartPlacementStrategy? = null
) : SmartGraphPanel<V, E>(graph, properties, placementStrategy, cssFile) {

    private val vertexContextMenu = ContextMenu()
    private val vertexMenuItemConsumers = mutableMapOf<MenuItem, Consumer<SmartGraphVertex<V>>>()
    private var vertexContextMenuPredicate: Predicate<SmartGraphVertex<V>>? = null

    init {
        enableContextMenu()
    }

    fun showVertexContextMenuIf(predicate: Predicate<SmartGraphVertex<V>>) {
        vertexContextMenuPredicate = predicate
    }

    fun addContextMenuItem(itemName: String, consumer: Consumer<SmartGraphVertex<V>>) {
        val menuItem = MenuItem(itemName)
        vertexContextMenu.items.add(menuItem)
        vertexMenuItemConsumers[menuItem] = consumer
    }

    @Suppress("UNCHECKED_CAST")
    private fun enableContextMenu() {
        setOnContextMenuRequested {
            val smartGraphVertex = UtilitiesJavaFX.pick(this, it.sceneX, it.sceneY) as? SmartGraphVertex<V>
                ?: return@setOnContextMenuRequested

            if (vertexContextMenuPredicate?.test(smartGraphVertex) == false) return@setOnContextMenuRequested

            vertexContextMenu.show(this, it.screenX, it.screenY)

            vertexMenuItemConsumers.forEach { (menuItem, consumer) ->
                menuItem.setOnAction {
                    consumer.accept(smartGraphVertex)
                }
            }
        }
    }
}