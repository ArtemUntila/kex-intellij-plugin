package org.vorpal.research.kex.plugin.util

class OptionList(private val section: String) : PrefixList() {

    override val prefix = "--option"

    fun addOption(optionName: String, value: Any) {
        val option = "$section:$optionName:$value"
        addPrefix()
        add(option)
    }
}