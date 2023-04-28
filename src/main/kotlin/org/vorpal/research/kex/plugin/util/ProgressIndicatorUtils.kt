package org.vorpal.research.kex.plugin.util

import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.progress.ProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val scope = CoroutineScope(Dispatchers.IO)

fun ProgressIndicator.onCanceled(checkIntervalMillis: Long, block: () -> Unit) {
    scope.launch {
        while (isRunning) {
            try {
                checkCanceled()
                delay(checkIntervalMillis)
            } catch (_: ProcessCanceledException) {
                block()
            }
        }
    }
}