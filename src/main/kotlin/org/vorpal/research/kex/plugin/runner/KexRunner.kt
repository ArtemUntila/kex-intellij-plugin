package org.vorpal.research.kex.plugin.runner

import com.intellij.openapi.project.Project
import org.vorpal.research.kex.plugin.command.DockerRunCommand

class KexRunner(project: Project) : AbstractKexRunner(project) {

    override fun dockerRunCommand(commandHelper: CommandHelper): DockerRunCommand {
        return commandHelper.default()
    }
}