package org.vorpal.research.kex.plugin.util

enum class Section {
    kex, testGen, concolic, executor, gui
}

data class Option(
    val section: Section,
    val name: String
) {
    override fun toString(): String {
        return "$section:$name"
    }
}