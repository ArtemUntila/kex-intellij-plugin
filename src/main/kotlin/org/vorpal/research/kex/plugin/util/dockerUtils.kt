package org.vorpal.research.kex.plugin.util

private const val DEPS = "/home/deps/"
private const val DOCKER_IMAGE = "arch-kex"
private const val KEX_OUTPUT = "/home/kex-output"

fun getDockerKexArgsList(localClasspathList: List<String>, output: String, target: String): List<String> {
    val containerClasspathList = localClasspathList.map { it.replace(Regex(".*[/\\\\]"), DEPS) }
    val containerClasspath = containerClasspathList.joinToString(":")

    val kexOutput = getKexOutput()
    println(kexOutput)
    val dockerArgsList = getDockerArgsList(localClasspathList, containerClasspathList, kexOutput)

    val kexArgsList = getKexArgsList(containerClasspath, target)
    val kexOptionsArgsList = getAllOptionsArgsList()

    return dockerArgsList + kexArgsList + kexOptionsArgsList
}

private fun getDockerArgsList(localClasspathList: List<String>, containerClasspathList: List<String>, kexOutput: String?): List<String> {
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

private fun getKexArgsList(classpath: String, target: String): List<String> {
    return listOf("--classpath", classpath, "--target", target, "--output", KEX_OUTPUT)
}
