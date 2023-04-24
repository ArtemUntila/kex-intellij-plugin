package org.vorpal.research.kex.plugin.util

import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.progress.ProgressIndicator
import kotlin.concurrent.thread

fun ProgressIndicator.onCanceled(checkIntervalMillis: Long, block: () -> Unit) {
    thread(start = true) {
        while (isRunning) {
            try {
                checkCanceled()
                Thread.sleep(checkIntervalMillis)
            } catch (_: ProcessCanceledException) {
                block()
            }
        }
    }
}