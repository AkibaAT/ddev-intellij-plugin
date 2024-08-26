package de.php_perfect.intellij.ddev.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UI
import de.php_perfect.intellij.ddev.DdevIntegrationBundle
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JComponent
import javax.swing.JPanel

class DdevSettingsComponent(project: Project?) {
    private val jPanel: JPanel
    private val checkForUpdatesCheckbox = JBCheckBox(DdevIntegrationBundle.message("settings.checkForUpdates"))
    private val watchDdevCheckbox = JBCheckBox(DdevIntegrationBundle.message("settings.watchDdev"))
    private val autoConfigureDataSource =
        JBCheckBox(DdevIntegrationBundle.message("settings.automaticConfiguration.autoConfigureDataSource"))
    private val autoConfigurePhpInterpreter =
        JBCheckBox(DdevIntegrationBundle.message("settings.automaticConfiguration.phpInterpreter"))
    private val autoConfigureNodeJsInterpreter =
        JBCheckBox(DdevIntegrationBundle.message("settings.automaticConfiguration.nodeJsInterpreter"))
    private val ddevBinary = TextFieldWithBrowseButton()

    init {
        val checkForUpdatesPanel = UI.PanelFactory.panel(this.checkForUpdatesCheckbox)
            .withComment(DdevIntegrationBundle.message("settings.checkForUpdates.description")).createPanel()
        val watchDdevPanel = UI.PanelFactory.panel(this.watchDdevCheckbox)
            .withComment(DdevIntegrationBundle.message("settings.watchDdev.description")).createPanel()

        val panel = JPanel()
        panel.setBorder(
            IdeBorderFactory.createTitledBorder(
                DdevIntegrationBundle.message("settings.automaticConfiguration"),
                true
            )
        )
        panel.setLayout(GridBagLayout())
        val gc = GridBagConstraints(
            0,
            GridBagConstraints.RELATIVE,
            1,
            1,
            1.0,
            0.0,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.BOTH,
            JBUI.emptyInsets(),
            0,
            0
        )
        panel.add(this.autoConfigureDataSource, gc)
        panel.add(this.autoConfigurePhpInterpreter, gc)
        panel.add(this.autoConfigureNodeJsInterpreter, gc)

        this.ddevBinary.addBrowseFolderListener(
            DdevIntegrationBundle.message("settings.chooseBinary.title"),
            "",
            project,
            FileChooserDescriptor(true, false, false, false, false, false)
        )

        this.jPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JBLabel(DdevIntegrationBundle.message("settings.ddevBinary")),
                this.ddevBinary,
                1,
                false
            )
            .addComponent(checkForUpdatesPanel, 1)
            .addComponent(watchDdevPanel, 1)
            .addComponent(panel, 1)
            .addComponentFillVertically(JPanel(), 0)
            .getPanel()
    }

    fun getPanel(): JPanel {
        return this.jPanel
    }

    fun getPreferredFocusedComponent(): JComponent {
        return this.checkForUpdatesCheckbox
    }

    fun getCheckForUpdatedStatus(): Boolean {
        return this.checkForUpdatesCheckbox.isSelected()
    }

    fun setCheckForUpdatesStatus(newStatus: Boolean) {
        this.checkForUpdatesCheckbox.setSelected(newStatus)
    }

    fun getWatchDdevCheckboxStatus(): Boolean {
        return this.watchDdevCheckbox.isSelected()
    }

    fun setAutoConfigureDataSource(newStatus: Boolean) {
        this.autoConfigureDataSource.setSelected(newStatus)
    }

    fun getAutoConfigureDataSource(): Boolean {
        return this.autoConfigureDataSource.isSelected()
    }

    fun setAutoConfigurePhpInterpreter(newStatus: Boolean) {
        this.autoConfigurePhpInterpreter.setSelected(newStatus)
    }

    fun getAutoConfigurePhpInterpreter(): Boolean {
        return this.autoConfigurePhpInterpreter.isSelected()
    }

    fun setAutoConfigureNodeJsInterpreter(newStatus: Boolean) {
        this.autoConfigureNodeJsInterpreter.setSelected(newStatus)
    }

    fun getAutoConfigureNodeJsInterpreter(): Boolean {
        return this.autoConfigureNodeJsInterpreter.isSelected()
    }

    fun setWatchDdevCheckboxStatus(newStatus: Boolean) {
        this.watchDdevCheckbox.setSelected(newStatus)
    }

    fun getDdevBinary(): String {
        return this.ddevBinary.getText()
    }

    fun setDdevBinary(ddevBinary: String) {
        this.ddevBinary.setText(ddevBinary)
    }
}
