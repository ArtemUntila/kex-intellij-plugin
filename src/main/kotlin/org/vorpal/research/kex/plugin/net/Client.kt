package org.vorpal.research.kex.plugin.net

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Client(private val host: String = "localhost", private val port: Int) {

    private var socket: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: PrintWriter? = null

    private var connected: Boolean = false

    val isConnected: Boolean
        get() = connected

    fun connect() {
        connected = try {
            socket = Socket(host, port)
            reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
            writer = PrintWriter(socket!!.getOutputStream(), true)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun receive(): String? {
        return try {
            reader!!.readLine()
        } catch (_: Exception) {
            null
        }
    }

    fun send(message: String): Boolean {
        return try {
            writer!!.println(message)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun close() {
        // reader and writer close() methods aren't called, because they are synchronized
        connected = false
        socket?.close()
    }
}