package org.vorpal.research.kex.plugin.runner

import org.vorpal.research.kex.plugin.DEPS
import org.vorpal.research.kex.plugin.DOCKER_IMAGE
import org.vorpal.research.kex.plugin.KEX_OUTPUT
import org.vorpal.research.kex.plugin.command.DockerRunCommand
import org.vorpal.research.kex.plugin.command.KexCommand
import org.vorpal.research.kex.plugin.settings.SettingsReader

class CommandHelper(
    classpath: Iterable<String>,
    private val target: String, private val testDir: String
) {

    companion object {
        private var id = 0
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

    private fun defaultDockerRunCommand(): DockerRunCommand {
        val dockerRunCommand = DockerRunCommand(DOCKER_IMAGE)
            .remove()
            .name("$DOCKER_IMAGE-${id++}")
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
        // TODO: Find something more efficient
        val list = localPath.split(System.getProperty("file.separator")).takeLast(5)
        return "$DEPS/" + list.joinToString("/")
    }
}