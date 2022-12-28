package org.vorpal.research.kex.plugin.args

class DockerKillArgs(private val containerName: String) : Args {

    override val list: List<String>
        get() = listOf("docker", "kill", containerName)
}