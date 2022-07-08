package org.vorpal.research.kex.plugin.util

const val TITLE = "Kex"

// TODO: Specify local path to kex project folder
const val KEX_PATH = "S:\\JetBrains\\IntelliJ IDEA\\Projects\\Summer2022\\kex"

fun getKEXArgsList(cp: String, target: String, output:String): List<String> {
    return listOf(
        "java",
        "-Xmx8g",
        "-Djava.security.manager",
        "-Djava.security.policy==kex.policy",
        "-Dlogback.statusListenerClass=ch.qos.logback.core.status.NopStatusListener",
        "-jar", "kex-runner/target/kex-runner-0.0.1-jar-with-dependencies.jar",
        "-cp", cp,
        "-t", target,
        "--output", output
    )
}