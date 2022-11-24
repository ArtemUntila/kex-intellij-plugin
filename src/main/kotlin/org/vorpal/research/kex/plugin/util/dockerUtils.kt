package org.vorpal.research.kex.plugin.util

//private const val DEPS = "/home/deps/"
//private const val DOCKER_IMAGE = "arch-kex"
//private const val KEX_OUTPUT = "/home/kex-output"

fun getDockerKexArgs(localClasspathList: List<String>, output: String, target: String): List<String> {
    val containerClasspathList = localClasspathList.map { it.replace(Regex(".*[/\\\\]"), DEPS) }
    val containerClasspath = containerClasspathList.joinToString(":")

    val kexOutput = getKexOutput()
    val dockerArgsList = getDockerArgs(localClasspathList, containerClasspathList, kexOutput)

    val kexArgsList = getKexArgs(containerClasspath, target)

    return dockerArgsList + kexArgsList// + kexOptionsArgsList
}

private fun getDockerArgs(localClasspathList: List<String>, containerClasspathList: List<String>, kexOutput: String?): List<String> {
    val dockerArgsList = mutableListOf("docker", "run", "--rm")

    for (i in localClasspathList.indices) {
        dockerArgsList.add("-v")
        dockerArgsList.add("${localClasspathList[i]}:${containerClasspathList[i]}")
    }

    if (kexOutput != null) {
        dockerArgsList.add("-v")
        dockerArgsList.add("$kexOutput:$KEX_OUTPUT")
    }

    dockerArgsList.add(DOCKER_IMAGE)

    return dockerArgsList
}

private fun getKexArgs(classpath: String, target: String): List<String> {
    val kexOptionsArgsList = getAllOptionArgs()
    val kexArgsList = listOf("--classpath", classpath, "--target", target, "--output", KEX_OUTPUT)
    return kexOptionsArgsList + kexArgsList
}
