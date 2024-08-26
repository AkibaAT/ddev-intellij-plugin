package de.php_perfect.intellij.ddev.state

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.StateInitializedListener
import de.php_perfect.intellij.ddev.settings.DdevSettingsState

class StartWatcherListener(private val project: Project) : StateInitializedListener {

    override fun onStateInitialized(state: State) {
        if (!DdevSettingsState.getInstance(this.project).watchDdev) {
            return
        }

        StateWatcher.Companion.getInstance(this.project)?.startWatching()
    }
}
