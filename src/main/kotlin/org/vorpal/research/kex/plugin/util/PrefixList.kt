package org.vorpal.research.kex.plugin.util

abstract class PrefixList : ArrayList<String>() {

    abstract val prefix: String

    fun addPrefix() {
        add(prefix)
    }
}