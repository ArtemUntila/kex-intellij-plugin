package org.vorpal.research.kex.plugin.args

import org.vorpal.research.kex.plugin.util.BindList

class DockerRunArgs(
    private val dockerImage: String,
    private val bindList: BindList,
    private val containerName: String
    ) : Args {

    override val list: List<String>
        get() = listOf("docker", "run", "--rm", "--name", containerName) + bindList + dockerImage
}