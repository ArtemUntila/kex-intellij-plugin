package org.vorpal.research.kex.plugin.net

import java.net.ServerSocket
import java.util.function.Predicate

fun findFreePort(): Int {
    ServerSocket(0).use {
        it.reuseAddress = true
        return it.localPort
    }
}

fun getConnectedClient(
    port: Int, timeout: Long,
    reconnectPause: Long = 1000,
    stopPredicate: Predicate<Client> = Predicate { it.isConnected }
): Client {
    val client = Client(port = port)

    var status = false
    var timeElapsed: Long = 0
    val startTime = System.currentTimeMillis()

    while (timeElapsed < timeout) {
        client.connect()
        if (stopPredicate.test(client)) {
            status = true
            break
        }
        Thread.sleep(reconnectPause)
        timeElapsed = System.currentTimeMillis() - startTime
    }

    when {
        !status -> throw ConnectionTimeoutException()
        !client.isConnected -> throw ConnectionFailedException()
        else -> return client
    }
}

class ConnectionTimeoutException : Exception()
class ConnectionFailedException : Exception()