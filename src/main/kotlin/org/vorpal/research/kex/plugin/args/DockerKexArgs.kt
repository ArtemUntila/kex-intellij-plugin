package org.vorpal.research.kex.plugin.args

import org.vorpal.research.kex.plugin.DEPS
import org.vorpal.research.kex.plugin.DOCKER_IMAGE
import org.vorpal.research.kex.plugin.KEX_OUTPUT
import org.vorpal.research.kex.plugin.settings.reader.SettingsReader
import org.vorpal.research.kex.plugin.util.*

class DockerKexArgs(
    private val localClasspathList: List<String>,
    private val target: String,
    private val testDir: String
    ) : Args {

    private companion object {
        var id = 0
    }

    private val containerClasspathList: List<String>
    val containerName: String

    init {
        id++
        containerClasspathList = localClasspathList.map { localToContainerPath(it) }
        containerName = "$DOCKER_IMAGE-$id"
    }

    override val list: List<String>
        get() = getDockerKexOptionArgs()

    private fun getDockerKexOptionArgs(): List<String> {
        // Docker args
        val bindList = getBinds()
        val dockerRunArgs = DockerRunArgs(DOCKER_IMAGE, bindList, containerName)

        // Kex args (main args + options)
        val containerClasspath = containerClasspathList.joinToString(":")
        val kexArgs = KexArgs(containerClasspath, target, KEX_OUTPUT)

        return dockerRunArgs.list + kexArgs.list
    }

    private fun getBinds(): BindList {
        val bindList = BindList()

        // Bind dependencies
        for (i in localClasspathList.indices) {
            bindList.addBind(localClasspathList[i], containerClasspathList[i])
        }

        // Bind output
        SettingsReader.getKexOutput()?.let {
            bindList.addBind(it, KEX_OUTPUT)
        }

        // Bind test directory
        bindList.addBind(testDir, "$KEX_OUTPUT/tests")

        return bindList
    }

    private fun localToContainerPath(localPath: String): String {
        val list = localPath.split(System.getProperty("file.separator")).takeLast(3)
        return "$DEPS/" + list.joinToString("/")
    }
}