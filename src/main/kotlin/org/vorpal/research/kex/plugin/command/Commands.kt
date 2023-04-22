package org.vorpal.research.kex.plugin.command

import org.vorpal.research.kex.plugin.DEPS
import org.vorpal.research.kex.plugin.DOCKER_IMAGE
import org.vorpal.research.kex.plugin.KEX_OUTPUT
import org.vorpal.research.kex.plugin.KEX_TEST
import org.vorpal.research.kex.plugin.settings.reader.SettingsReader

private var dockerRunId = 0

fun dockerRunCommand(): DockerRunCommand {
    return DockerRunCommand(DOCKER_IMAGE)
        .remove()
        .name("$DOCKER_IMAGE-${dockerRunId++}")
}

fun dockerRunKexCommand(classpath: Iterable<String>, target: String, testDir: String): DockerRunCommand {
    val dockerRunCommand = dockerRunCommand().addVolume(testDir, KEX_TEST)

    SettingsReader.kexOutputDir?.let {
        dockerRunCommand.addVolume(it, KEX_OUTPUT)
    }

    val classpathMap = classpath.associateWith { localToContainerPath(it) }
    classpathMap.forEach { (localPath, containerPath) ->
        dockerRunCommand.addVolume(localPath, containerPath)
    }

    val kexCommand = kexCommand(classpathMap.values, target, KEX_OUTPUT)

    return dockerRunCommand.containerCommand(kexCommand)
}

fun kexCommand(classpath: Iterable<String>, target: String, output: String): KexCommand {
    val kexCommand = KexCommand(classpath, target, output)
    SettingsReader.allOptions.forEach {
        kexCommand.addOption(it)
    }
    return kexCommand
}

private fun localToContainerPath(localPath: String): String {
    val list = localPath.split(System.getProperty("file.separator")).takeLast(5)
    return "$DEPS/" + list.joinToString("/")
}