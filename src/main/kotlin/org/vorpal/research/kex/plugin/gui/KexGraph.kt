package org.vorpal.research.kex.plugin.gui

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList
import com.brunomnsilva.smartgraph.graph.Edge
import com.brunomnsilva.smartgraph.graph.Vertex
import com.brunomnsilva.smartgraph.graphview.SmartLabelSource
import kotlinx.serialization.Serializable


@Serializable
data class KexVertex(
    val id: Int,
    @get:SmartLabelSource
    val name: String
) {
    override fun toString(): String = "KexVertex(id=$id, name='$name')"
}

data class KexEdge(
    val up: KexVertex,
    val down: KexVertex
) {
    override fun toString(): String {
        return "KexEdge(up=${up}, down=$down)"
    }
}

class KexGraph : DigraphEdgeList<KexVertex, KexEdge>() {

    fun addVertex(vertex: KexVertex): Boolean {
        return insertVertexOrNull(vertex) != null
    }

    fun addEdge(up: KexVertex, down: KexVertex, edge: KexEdge): Boolean {
        return insertEdgeOrNull(up, down, edge) != null
    }

    fun addEdge(edge: KexEdge) = addEdge(edge.up, edge.down, edge)

    fun addEdge(up: KexVertex, down: KexVertex) = addEdge(up, down, KexEdge(up, down))

    fun insertVertexOrNull(vertex: KexVertex): Vertex<KexVertex>? {
        return try {
            this.insertVertex(vertex)
        } catch (_: RuntimeException) {
            null
        }
    }

    fun insertEdgeOrNull(up: KexVertex, down: KexVertex, edge: KexEdge): Edge<KexEdge, KexVertex>? {
        return try {
            this.insertEdge(up, down, edge)
        } catch (_: RuntimeException) {
            null
        }
    }

    fun insertEdgeOrNull(up: KexVertex, down: KexVertex) = insertEdgeOrNull(up, down, KexEdge(up, down))

    fun insertEdgeOrNull(edge: KexEdge) = insertEdgeOrNull(edge.up, edge.down, edge)
}