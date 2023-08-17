package org.vorpal.research.kex.plugin.runner

import org.vorpal.research.kex.plugin.KEX_OUTPUT
import org.vorpal.research.kex.plugin.KEX_PORT
import org.vorpal.research.kex.plugin.command.DockerRunCommand
import org.vorpal.research.kex.plugin.command.KexCommand
import org.vorpal.research.kex.plugin.settings.SettingsReader
import org.vorpal.research.kex.plugin.util.Section
import kotlin.random.Random

class CommandHelper(
    classpath: Iterable<String>,
    private val target: String,
    private val testDir: String
) {

    private companion object {
        val isWindows = System.getProperty("os.name").startsWith("Windows")
    }

    private val classpathMap: Map<String, String> = classpath.associateWith { localToContainerPath(it) }
    private val dockerRunCommand: DockerRunCommand = defaultDockerRunCommand()
    private val kexCommand: KexCommand = defaultKexCommand()

    fun default(): DockerRunCommand = dockerRunCommand.containerCommand(kexCommand)

    fun gui(port: Int): DockerRunCommand {
        dockerRunCommand.addPort(port, KEX_PORT)
        kexCommand.addOption(Section.gui, "enabled", "true")
        return default()
    }

    private fun defaultDockerRunCommand(): DockerRunCommand {
        val dockerRunCommand = DockerRunCommand(SettingsReader.dockerImage)
            .name(generateContainerName())
            .addVolume(testDir, "$KEX_OUTPUT/${SettingsReader.testsDir}")

        if (SettingsReader.dockerRemove) dockerRunCommand.remove()

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
            .addOptions(SettingsReader.testGenOptions)
            .addOptions(SettingsReader.concolicOptions)
            .addOptions(SettingsReader.executorOptions)
    }

    private fun generateContainerName(): String {
        return Integer.toHexString(Random.nextInt())
    }

    private fun localToContainerPath(localPath: String): String {
        return if (isWindows) "/" + localPath.replace(":", "").replace("\\", "/")
        else localPath
    }
}