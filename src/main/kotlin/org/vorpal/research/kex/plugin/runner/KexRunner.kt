package org.vorpal.research.kex.plugin.runner

import com.intellij.openapi.module.Module
import org.vorpal.research.kex.plugin.command.DockerRunCommand

class KexRunner(module: Module) : AbstractKexRunner(module) {

    override fun dockerRunCommand(commandHelper: CommandHelper): DockerRunCommand {
        return commandHelper.default()
    }
}