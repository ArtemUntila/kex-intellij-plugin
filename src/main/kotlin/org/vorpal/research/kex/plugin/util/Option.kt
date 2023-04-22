package org.vorpal.research.kex.plugin.util

data class Option(
    val section: Section,
    val name: String,
    val value: Any
) {
    override fun toString(): String {
        return "$section:$name:$value"
    }
}
