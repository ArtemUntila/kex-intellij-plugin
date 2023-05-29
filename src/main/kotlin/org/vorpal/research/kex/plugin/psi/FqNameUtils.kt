package org.vorpal.research.kex.plugin.psi

import org.jetbrains.kotlin.name.FqName

// Temporary solution from intellij sdk 2022.1 (org.jetbrains.kotlin.idea.util.toJvmFqName)
/**
 * it is impossible to unambiguously convert "fqName" into "jvmFqName" without additional information
 */
val FqName.toJvmFqName: String
    get() {
        val asString = asString()
        var startIndex = 0
        while (startIndex != -1) { // always true
            val dotIndex = asString.indexOf('.', startIndex)
            if (dotIndex == -1) return asString

            startIndex = dotIndex + 1
            val charAfterDot = asString.getOrNull(startIndex) ?: return asString
            if (!charAfterDot.isLetter()) return asString
            if (charAfterDot.isUpperCase()) return buildString {
                append(asString.subSequence(0, startIndex))
                append(asString.substring(startIndex).replace('.', '$'))
            }
        }

        return asString
    }