package org.vorpal.research.kex.plugin.command

import org.vorpal.research.kex.plugin.util.Option
import org.vorpal.research.kex.plugin.util.Section

class KexCommand(
    private val classpath: Iterable<String>,
    private val target: String,
    private val output: String
) : Command {

    private val optionValueMap: MutableMap<Option, String> = mutableMapOf()

    fun addOption(option: Option, value: String): KexCommand {
        optionValueMap[option] = value
        return this
    }

    fun addOption(section: Section, name: String, value: String): KexCommand {
        return this.addOption(Option(section, name), value)
    }

    fun addOptions(options: Map<Option, String>): KexCommand {
        optionValueMap.putAll(options)
        return this
    }

    override fun args(): List<String> {
        val args = ArgsList()

        args.addOption("--classpath", classpath.joinToString(":"))
        args.addOption("--target", target)
        args.addOption("--output", output)
        args.addBindOptions("--option", optionValueMap)

        return args
    }
}