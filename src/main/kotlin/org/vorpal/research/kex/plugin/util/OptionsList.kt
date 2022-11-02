package org.vorpal.research.kex.plugin.util

class OptionsList(private val section: String) {

    private val _list = mutableListOf<String>()

    val list: List<String>
        get() = _list

    fun add(option: String, value: Any) {
        _list.add("--option")
        _list.add("$section:$option:$value")
    }
}