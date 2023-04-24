package org.vorpal.research.kex.plugin.util

import com.intellij.openapi.module.Module
import com.intellij.openapi.project.guessModuleDir
import com.intellij.openapi.project.rootManager
import com.intellij.openapi.roots.OrderEnumerator
import com.intellij.testIntegration.createTest.CreateTestAction
import org.jetbrains.jps.model.java.JavaSourceRootType

fun Module.getTestDirPath(): String {
    val testModule = CreateTestAction.suggestModuleForTests(project, this)
    val testSources = testModule.rootManager.getSourceRoots(JavaSourceRootType.TEST_SOURCE)
    return testSources.firstOrNull()?.path ?: "${guessModuleDir()!!.path}/kex-tests"
}

fun Module.getClasspathList(): List<String> {
    val classpathList = OrderEnumerator.orderEntries(this).recursively().pathsList.pathList
    return classpathList.filter { !it.contains("jdk") }  // TODO: Find something more efficient
}