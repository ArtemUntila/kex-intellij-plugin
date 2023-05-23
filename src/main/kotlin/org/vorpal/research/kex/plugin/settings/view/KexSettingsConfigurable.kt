package org.vorpal.research.kex.plugin.settings.view

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
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
                    .align(AlignX.FILL)
                    .bindText(state::outputDir)
            }.enabledIf(cb.selected)
            group("Docker") {
                row("Image:") {
                    textField().bindText(state::dockerImage)
                }
            }
            group("GUI") {
                row("Connection timeout:") {
                    intTextField(IntRange(1, 1_000_000))
                        .bindIntText(state::guiConnectionTimeout)
                        .gap(RightGap.SMALL)
                    label("seconds")
                }
            }
        }
    }
}