package org.vorpal.research.kex.plugin.settings

import com.intellij.codeInspection.javaDoc.JavadocUIUtil.bindItem
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*

class TestGenOptionsConfigurable : BoundConfigurable("Test Generation") {

    private val state = TestGenOptionsStateComponent.instance.state

    override fun createPanel(): DialogPanel {
        lateinit var cb: Cell<JBCheckBox>
        return panel {
            row {
                cb = checkBox("Enabled").bindSelected(state::enabled)
            }
            rowsRange {
                row("Tests directory:") {
                    textField().bindText(state::testsDir)
                }
                row("Access level:") {
                    comboBox(listOf("private", "protected", "package", "public")).bindItem(state::accessLevel)
                }
                buttonsGroup {
                    row("Test case language:") {
                        radioButton("Java", "java")
                        radioButton("Kotlin", "kotlin")
                    }.enabledIf(cb.selected)
                }.bind(state::testCaseLanguage)
                row {
                    checkBox("Generate setup").bindSelected(state::generateSetup)
                }
                row {
                    checkBox("Log JUnit").bindSelected(state::logJUnit)
                }
                row("Test timeout:") {
                    intTextField().bindIntText(state::testTimeout)
                }
            }.enabledIf(cb.selected)
        }
    }
}