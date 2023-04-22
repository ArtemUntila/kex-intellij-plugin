package org.vorpal.research.kex.plugin.command

class DockerKillCommand(private val containerName: String) : Command {

    override fun args(): List<String> {
        return listOf("docker", "kill", containerName)
    }
}