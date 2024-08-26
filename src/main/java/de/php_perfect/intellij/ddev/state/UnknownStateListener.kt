package de.php_perfect.intellij.ddev.state

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.StateChangedListener
import de.php_perfect.intellij.ddev.notification.DdevNotifier

class UnknownStateListener(project: Project) : StateChangedListener {
    private val project: Project

    init {
        this.project = project
    }

    override fun onDdevChanged(state: State) {
        if (!state.isAvailable() || !state.isConfigured()) {
            return
        }

        val description = state.getDescription()

        if (description == null || description.getStatus() == null) {
            DdevNotifier.getInstance(this.project)!!.notifyUnknownStateEntered()
        }
    }
}
