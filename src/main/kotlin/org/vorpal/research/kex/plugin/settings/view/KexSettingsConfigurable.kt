package org.vorpal.research.kex.plugin.settings.view

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import org.vorpal.research.kex.plugin.settings.state.KexSettingsStateComponent

class KexSettingsConfigurable : BoundConfigurable("Kex") {

    private val state = KexSettingsStateComponent.instance.state

    override fun createPanel(): DialogPanel {
        lateinit var cb: Cell<JBCheckBox>
        return panel {
            row {
                cb = checkBox("Kex output").bindSelected(state::kexOutput)
            }
            row("Output directory:") {
                val fcd = FileChooserDescriptorFactory.createSingleFolderDescriptor()
                textFieldWithBrowseButton("Select Output Directory", null, fcd)
                    .horizontalAlign(HorizontalAlign.FILL)
                    .bindText(state::outputDir)
            }.enabledIf(cb.selected)
            // TODO: group title
            group {
                row("Docker image:") {
                    textField().bindText(state::dockerImage)
                }
            }
        }
    }
}