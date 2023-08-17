package org.vorpal.research.kex.plugin.command

class DockerRunCommand(private val image: String) : Command {

    private var remove: Boolean = false
    private var name: String? = null
    private val ports: MutableMap<Int, Int> = mutableMapOf()
    private val volumes: MutableMap<String, String> = mutableMapOf()
    private var containerCommand: Command? = null

    val containerName: String?
        get() = name

    fun remove() = apply {
        remove = true
    }

    fun name(name: String) = apply {
        this.name = name
    }

    fun addPort(localPort: Int, containerPort: Int) = apply {
        ports[localPort] = containerPort
    }

    fun addVolume(localPath: String, containerPath: String) = apply {
        volumes[localPath] = containerPath
    }

    fun containerCommand(command: Command) = apply {
        containerCommand = command
    }

    override fun args(): List<String> {
        val args = ArgsList("docker", "run")

        if (remove) args.add("--rm")

        name?.let { args.addOption("--name", it) }

        args.addBindOptions("-p", ports)
        args.addBindOptions("-v", volumes)

        args.add(image)

        containerCommand?.let {
            args.addAll(it.args())
        }

        return args
    }
}