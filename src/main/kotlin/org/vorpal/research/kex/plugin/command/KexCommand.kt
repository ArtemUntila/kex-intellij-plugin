package org.vorpal.research.kex.plugin.command

import org.vorpal.research.kex.plugin.util.Option

class KexCommand(
    private val classpath: Iterable<String>,
    private val target: String,
    private val output: String
) : Command {

    private val options: MutableList<Option> = mutableListOf()

    fun addOption(option: Option): KexCommand {
        options.add(option)
        return this
    }

    override fun args(): List<String> {
        val args = mutableListOf<String>()

        val classpath = classpath.joinToString(":")
        args.add("--classpath")
        args.add(classpath)

        args.add("--target")
        args.add(target)

        args.add("--output")
        args.add(output)

        options.forEach {
            args.add("--option")
            args.add("$it")
        }

        return args
    }
}