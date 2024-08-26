package de.php_perfect.intellij.ddev.version

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.StateInitializedListener
import de.php_perfect.intellij.ddev.notification.DdevNotifier.Companion.getInstance
import de.php_perfect.intellij.ddev.settings.DdevSettingsState
import de.php_perfect.intellij.ddev.settings.DdevSettingsState.Companion.getInstance
import de.php_perfect.intellij.ddev.state.DdevStateManager.Companion.getInstance
import de.php_perfect.intellij.ddev.state.State

class CheckVersionListener(project: Project) : StateInitializedListener {
    private val project: Project

    init {
        this.project = project
    }

    override fun onStateInitialized(state: State) {
        if (DdevSettingsState.getInstance(this.project).checkForUpdates) {
            VersionChecker.Companion.getInstance(this.project).checkDdevVersion()
        }
    }
}
