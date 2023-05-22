package org.vorpal.research.kex.plugin.runner

import com.intellij.openapi.project.Project
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.kotlin.idea.util.application.executeOnPooledThread
import org.vorpal.research.kex.plugin.TITLE
import org.vorpal.research.kex.plugin.command.DockerRunCommand
import org.vorpal.research.kex.plugin.gui.KexGraphPanel
import org.vorpal.research.kex.plugin.gui.KexVertex
import org.vorpal.research.kex.plugin.gui.KexWindow
import org.vorpal.research.kex.plugin.net.ConnectionFailedException
import org.vorpal.research.kex.plugin.net.ConnectionTimeoutException
import org.vorpal.research.kex.plugin.net.findFreePort
import org.vorpal.research.kex.plugin.net.getConnectedClient
import org.vorpal.research.kex.plugin.settings.SettingsReader
import org.vorpal.research.kex.plugin.util.notifyError

class KexGUIRunner(private val project: Project) : AbstractKexRunner(project) {

    private val port = findFreePort()

    override fun dockerRunCommand(commandHelper: CommandHelper): DockerRunCommand {
        return commandHelper.gui(port)
    }

    override fun postRun(target: String, backgroundable: CommandBackgroundable) {
        executeOnPooledThread {
            runGUI(target, backgroundable)
        }
    }

    private fun runGUI(target: String, backgroundable: CommandBackgroundable) {
        val timeout = SettingsReader.guiConnectionTimeout
        val client = try {
            getConnectedClient(port, timeout * 1000L) {
                !backgroundable.isRunning || it.receive() == "INIT"
            }
        } catch (_: ConnectionTimeoutException) {
            notifyError(project, TITLE, "Couldn't connect to GUI server within $timeout seconds")
            return
        } catch (_: ConnectionFailedException) {
            return
        }

        val graphPanel = KexGraphPanel()
        graphPanel.onPathVertexReverse { pathVertex ->
            client.send(Json.encodeToString(pathVertex))
        }
        graphPanel.onNext {
            client.send("NEXT")
        }

        val window = KexWindow(project, target, graphPanel)
        window.onClosed {
            client.close()
        }

        while (true) {
            val json = client.receive() ?: break
            val vertices: List<KexVertex> = Json.decodeFromString(json)
            graphPanel.addVerticesAsTrace(vertices)
        }

        graphPanel.stopControls()
        client.close()
    }
}