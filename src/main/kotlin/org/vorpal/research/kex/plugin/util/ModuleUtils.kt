package org.vorpal.research.kex.plugin.util

import com.intellij.openapi.module.Module
import com.intellij.openapi.project.guessModuleDir
import com.intellij.openapi.project.rootManager
import com.intellij.openapi.roots.OrderEnumerator
import com.intellij.testIntegration.createTest.CreateTestAction
import org.jetbrains.jps.model.java.JavaSourceRootType

fun Module.getTestDirPath(default: String): String {
    val testModule = CreateTestAction.suggestModuleForTests(project, this)
    val testSources = testModule.rootManager.getSourceRoots(JavaSourceRootType.TEST_SOURCE)
    return testSources.firstOrNull()?.path ?: "${guessModuleDir()!!.path}/$default"
}

val Module.orderEnumerator: OrderEnumerator
    get() = OrderEnumerator.orderEntries(this)

fun Module.getClasspathList(): List<String> = orderEnumerator.withoutSdk().productionOnly().pathsList.pathList