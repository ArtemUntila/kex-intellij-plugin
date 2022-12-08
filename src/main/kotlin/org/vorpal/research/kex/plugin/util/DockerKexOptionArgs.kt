package org.vorpal.research.kex.plugin.util

class DockerKexOptionArgs(
    private val localClasspathList: List<String>,
    private val target: String,
    private val testDir: String
    ) : Args {
    override val list: List<String>
        get() = getDockerKexOptionArgs()

    private fun getDockerKexOptionArgs(): List<String> {
        val containerClasspathList = localClasspathList.map { localToContainerPath(it) }

        val containerClasspath = containerClasspathList.joinToString(":")

        val localOutput = SettingsReader.getKexOutput()

        val dockerArgs = DockerArgs(DOCKER_IMAGE, localClasspathList, containerClasspathList, localOutput, KEX_OUTPUT, testDir)
        val kexArgs = KexArgs(containerClasspath, target, KEX_OUTPUT)
        val optionArgs = OptionArgs()

        return dockerArgs.list + kexArgs.list + optionArgs.list
    }

    private fun localToContainerPath(localPath: String): String {
        val list = localPath.split(System.getProperty("file.separator")).takeLast(3)
        return "$DEPS/" + list.joinToString("/")
    }
}