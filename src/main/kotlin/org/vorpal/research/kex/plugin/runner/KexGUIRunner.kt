package org.vorpal.research.kex.plugin.runner

import com.intellij.openapi.project.Project
import org.vorpal.research.kex.plugin.command.DockerRunCommand

class KexGUIRunner(project: Project) : AbstractKexRunner(project) {

    override fun dockerRunCommand(commandHelper: CommandHelper): DockerRunCommand {
        TODO("Not yet implemented")
    }
}