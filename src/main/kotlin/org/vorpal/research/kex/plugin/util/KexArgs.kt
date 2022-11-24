package org.vorpal.research.kex.plugin.util

class KexArgs(
    private val classpath: String,
    private val target: String,
    private val output: String
    ) : Args {

    override val list: List<String>
        get() = listOf("--classpath", classpath, "--target", target, "--output", output)
}