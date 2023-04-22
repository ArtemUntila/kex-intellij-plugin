package org.vorpal.research.kex.plugin.command

class DockerRunCommand(private val image: String) : Command {

    private var remove: Boolean = false
    private var name: String? = null
    private val ports: MutableMap<Int, Int> = mutableMapOf()
    private val volumes: MutableMap<String, String> = mutableMapOf()
    private var containerCommand: Command? = null

    val containerName: String?
        get() = name

    fun remove(): DockerRunCommand {
        remove = true
        return this
    }

    fun name(name: String): DockerRunCommand {
        this.name = name
        return this
    }

    fun addPort(localPort: Int, containerPort: Int): DockerRunCommand {
        ports[localPort] = containerPort
        return this
    }

    fun addVolume(localPath: String, containerPath: String): DockerRunCommand {
        volumes[localPath] = containerPath
        return this
    }

    fun containerCommand(command: Command): DockerRunCommand {
        containerCommand = command
        return this
    }

    override fun args(): List<String> {
        val args = mutableListOf("docker", "run")

        if (remove) args.add("--rm")

        name?.let {
            args.add("--name")
            args.add(it)
        }

        ports.forEach { (localPort, containerPort) ->
            args.add("-p")
            args.add("$localPort:$containerPort")
        }

        volumes.forEach { (localPath, containerPath) ->
            args.add("-v")
            args.add("$localPath:$containerPath")
        }

        args.add(image)

        containerCommand?.let {
            args.addAll(it.args())
        }

        return args
    }
}