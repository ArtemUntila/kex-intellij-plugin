package org.vorpal.research.kex.plugin.util

class DockerArgs(
    private val dockerImage: String,
    private val localClasspathList: List<String>,
    private val containerClasspathList: List<String>,
    private val localOutput: String?,
    private val containerOutput: String,
    private val testDir: String
    ) : Args {

    override val list: List<String>
        get() = getDockerArgs()

    private fun getDockerArgs(): List<String> {
        val dockerArgs = mutableListOf("docker", "run", "--rm", "--name", "123")

        // Bind dependencies
        for (i in localClasspathList.indices) {
            dockerArgs.add("-v")
            dockerArgs.add("${localClasspathList[i]}:${containerClasspathList[i]}")
        }

        // Bind output
        if (localOutput != null) {
            dockerArgs.add("-v")
            dockerArgs.add("$localOutput:$containerOutput")
        }

        // Bind test directory
        dockerArgs.add("-v")
        dockerArgs.add("$testDir:$containerOutput/tests")

        dockerArgs.add(dockerImage)

        return dockerArgs
    }
}