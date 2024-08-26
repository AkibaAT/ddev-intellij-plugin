package de.php_perfect.intellij.ddev.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.ThrowableComputable
import de.php_perfect.intellij.ddev.DdevIntegrationBundle
import de.php_perfect.intellij.ddev.cmd.CommandFailedException
import de.php_perfect.intellij.ddev.cmd.Ddev.Companion.getInstance
import de.php_perfect.intellij.ddev.state.DdevStateManager
import de.php_perfect.intellij.ddev.state.StateWatcher
import de.php_perfect.intellij.ddev.version.Version
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class DdevSettingsConfigurable(project: Project) : Configurable {
    private var ddevSettingsComponent: DdevSettingsComponent? = null

    private val project: Project

    init {
        this.project = project
    }

    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return getName()
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return this.ddevSettingsComponent!!.getPreferredFocusedComponent()
    }

    override fun createComponent(): JComponent {
        this.ddevSettingsComponent = DdevSettingsComponent(this.project)

        return this.ddevSettingsComponent!!.getPanel()
    }

    override fun isModified(): Boolean {
        val settings: DdevSettingsState = DdevSettingsState.Companion.getInstance(this.project)
        var modified = this.ddevSettingsComponent!!.getDdevBinary() != settings.ddevBinary
        modified = modified or (this.ddevSettingsComponent!!.getCheckForUpdatedStatus() != settings.checkForUpdates)
        modified = modified or (this.ddevSettingsComponent!!.getWatchDdevCheckboxStatus() != settings.watchDdev)
        modified =
            modified or (this.ddevSettingsComponent!!.getAutoConfigureDataSource() != settings.autoConfigureDataSource)
        modified =
            modified or (this.ddevSettingsComponent!!.getAutoConfigurePhpInterpreter() != settings.autoConfigurePhpInterpreter)
        modified =
            modified or (this.ddevSettingsComponent!!.getAutoConfigureNodeJsInterpreter() != settings.autoConfigureNodeJsInterpreter)

        return modified
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
        val newBinary = this.ddevSettingsComponent!!.getDdevBinary()

        if (!newBinary.isEmpty()) {
            verifyBinary(newBinary)
        }

        val settings: DdevSettingsState = DdevSettingsState.Companion.getInstance(this.project)
        settings.ddevBinary = newBinary
        settings.checkForUpdates = this.ddevSettingsComponent!!.getCheckForUpdatedStatus()
        settings.watchDdev = this.ddevSettingsComponent!!.getWatchDdevCheckboxStatus()
        settings.autoConfigureDataSource = this.ddevSettingsComponent!!.getAutoConfigureDataSource()
        settings.autoConfigurePhpInterpreter = this.ddevSettingsComponent!!.getAutoConfigurePhpInterpreter()
        settings.autoConfigureNodeJsInterpreter = this.ddevSettingsComponent!!.getAutoConfigureNodeJsInterpreter()

        StateWatcher.getInstance(this.project)?.stopWatching()
        ApplicationManager.getApplication()
            .executeOnPooledThread(Runnable { DdevStateManager.getInstance(this.project).reinitialize() })
    }

    @Throws(ConfigurationException::class)
    private fun verifyBinary(newBinary: String) {
        try {
            ProgressManager.getInstance().runProcessWithProgressSynchronously<Version?, CommandFailedException?>(
                ThrowableComputable { getInstance()!!.version(newBinary, project) },
                DdevIntegrationBundle.message("settings.process.verifyBinary.title"),
                false,
                this.project
            )
        } catch (exception: CommandFailedException) {
            throw ConfigurationException(
                DdevIntegrationBundle.message("settings.validation.invalidBinary.message", newBinary),
                exception,
                DdevIntegrationBundle.message("settings.validation.invalidBinary.title")
            )
        }
    }

    override fun reset() {
        val settings: DdevSettingsState = DdevSettingsState.Companion.getInstance(this.project)
        this.ddevSettingsComponent!!.setDdevBinary(settings.ddevBinary)
        this.ddevSettingsComponent!!.setCheckForUpdatesStatus(settings.checkForUpdates)
        this.ddevSettingsComponent!!.setWatchDdevCheckboxStatus(settings.watchDdev)
        this.ddevSettingsComponent!!.setAutoConfigureDataSource(settings.autoConfigureDataSource)
        this.ddevSettingsComponent!!.setAutoConfigurePhpInterpreter(settings.autoConfigurePhpInterpreter)
        this.ddevSettingsComponent!!.setAutoConfigureNodeJsInterpreter(settings.autoConfigureNodeJsInterpreter)
    }

    override fun disposeUIResources() {
        this.ddevSettingsComponent = null
    }

    companion object {
        fun getName(): String {
            return DdevIntegrationBundle.message("settings.title")
        }
    }
}
