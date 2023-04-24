package org.vorpal.research.kex.plugin.command

import org.vorpal.research.kex.plugin.DEPS
import org.vorpal.research.kex.plugin.DOCKER_IMAGE
import org.vorpal.research.kex.plugin.KEX_OUTPUT
import org.vorpal.research.kex.plugin.KEX_TEST
import org.vorpal.research.kex.plugin.settings.reader.SettingsReader

private var dockerRunId = 0

fun dockerRunKexCommand(classpath: Iterable<String>, target: String, testDir: String): DockerRunCommand {
    val dockerRunCommand = DockerRunCommand(DOCKER_IMAGE)
        .remove()
        .name("$DOCKER_IMAGE-${dockerRunId++}")
        .addVolume(testDir, KEX_TEST)

    SettingsReader.kexOutputDir?.let {
        dockerRunCommand.addVolume(it, KEX_OUTPUT)
    }

    val classpathMap = classpath.associateWith { localToContainerPath(it) }
    classpathMap.forEach { (localPath, containerPath) ->
        dockerRunCommand.addVolume(localPath, containerPath)
    }

    val kexCommand = KexCommand(classpathMap.values, target, KEX_OUTPUT)
        .addOptions(SettingsReader.kexOptions)
        .addOptions(SettingsReader.concolicOptions)
        .addOptions(SettingsReader.testGenOptions)
        .addOptions(SettingsReader.executorOptions)

    return dockerRunCommand.containerCommand(kexCommand)
}

private fun localToContainerPath(localPath: String): String {
    val list = localPath.split(System.getProperty("file.separator")).takeLast(5)
    return "$DEPS/" + list.joinToString("/")
}