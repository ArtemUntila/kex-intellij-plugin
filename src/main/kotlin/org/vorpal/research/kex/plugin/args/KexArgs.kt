package org.vorpal.research.kex.plugin.args

import org.vorpal.research.kex.plugin.settings.reader.SettingsReader

class KexArgs(
    private val classpath: String,
    private val target: String,
    private val output: String
    ) : Args {

    override val list: List<String>
        get() = listOf("--classpath", classpath, "--target", target, "--output", output) + OptionArgs.list

    object OptionArgs : Args {
        override val list: List<String>
            get() = SettingsReader.kexOptions +
                    SettingsReader.testGenOptions +
                    SettingsReader.concolicOptions +
                    SettingsReader.executorOptions
    }
}