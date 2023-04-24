package org.vorpal.research.kex.plugin.command

class ArgsList() : ArrayList<String>() {

    constructor(vararg elements: String) : this() {
        addAll(elements)
    }

    private fun addOption(option: String, value: String) {
        super.add(option)
        super.add(value)
    }

    fun addOption(option: String, value: Any) {
        addOption(option, "$value")
    }

    fun addBindOption(option: String, pair: Pair<Any, Any>) {
        addOption(option, "${pair.first}:${pair.second}")
    }

    fun <K : Any, V : Any> addBindOptions(option: String, nameValueMap: Map<K, V>) {
        nameValueMap.toList().forEach {
            addBindOption(option, it)
        }
    }
}