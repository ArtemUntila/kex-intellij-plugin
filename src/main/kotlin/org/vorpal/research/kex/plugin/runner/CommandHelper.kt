package org.vorpal.research.kex.plugin.runner

import org.vorpal.research.kex.plugin.KEX_OUTPUT
import org.vorpal.research.kex.plugin.KEX_PORT
import org.vorpal.research.kex.plugin.command.DockerRunCommand
import org.vorpal.research.kex.plugin.command.KexCommand
import org.vorpal.research.kex.plugin.settings.SettingsReader
import org.vorpal.research.kex.plugin.util.Section

class CommandHelper(
    classpath: Iterable<String>,
    private val target: String,
    private val testDir: String
) {

    private companion object {
        var id = 0
        val isWindows = System.getProperty("os.name").startsWith("Windows")
    }

    private val classpathMap: Map<String, String>
    private val dockerRunCommand: DockerRunCommand
    private val kexCommand: KexCommand

    init {
        classpathMap = classpath.associateWith { localToContainerPath(it) }
        dockerRunCommand = defaultDockerRunCommand()
        kexCommand = defaultKexCommand()
    }

    fun default(): DockerRunCommand = dockerRunCommand.containerCommand(kexCommand)

    fun gui(port: Int): DockerRunCommand {
        dockerRunCommand.addPort(port, KEX_PORT)
        kexCommand.addOption(Section.concolic, "searchStrategy", "${SettingsReader.searchStrategy}-gui")
        return default()
    }

    private fun defaultDockerRunCommand(): DockerRunCommand {
        val dockerImage = SettingsReader.dockerImage
        val dockerRunCommand = DockerRunCommand(dockerImage)
            .remove()
            .name("$dockerImage-${id++}")
            .addVolume(testDir, "$KEX_OUTPUT/${SettingsReader.testsDir}")

        SettingsReader.kexOutputDir?.let {
            dockerRunCommand.addVolume(it, KEX_OUTPUT)
        }

        classpathMap.forEach { (localPath, containerPath) ->
            dockerRunCommand.addVolume(localPath, containerPath)
        }

        return dockerRunCommand
    }

    private fun defaultKexCommand(): KexCommand {
        return KexCommand(classpathMap.values, target, KEX_OUTPUT)
            .addOptions(SettingsReader.kexOptions)
            .addOptions(SettingsReader.concolicOptions)
            .addOptions(SettingsReader.testGenOptions)
            .addOptions(SettingsReader.executorOptions)
    }

    private fun localToContainerPath(localPath: String): String {
        return if (isWindows) "/" + localPath.replace(":", "").replace("\\", "/")
        else localPath
    }
}