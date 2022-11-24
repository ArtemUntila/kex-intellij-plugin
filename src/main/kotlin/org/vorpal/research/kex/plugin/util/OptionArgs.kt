package org.vorpal.research.kex.plugin.util

class OptionArgs : Args {

    override val list: List<String>
        get() = SettingsReader.kexOptions + SettingsReader.testGenOptions + SettingsReader.concolicOptions + SettingsReader.executorOptions
}