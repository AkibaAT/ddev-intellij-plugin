package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.cmd.DdevRunner
import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.state.DdevStateManager
import de.php_perfect.intellij.ddev.state.State

class DdevStartAction : DdevRunAction() {
    override fun run(project: Project) {
        DdevRunner.getInstance().start(project)
    }

    override fun isActive(project: Project): Boolean {
        val state: State = DdevStateManager.getInstance(project).state

        if (!state.isAvailable || !state.isConfigured) {
            return false
        }

        val description: Description? = state.description

        if (description == null) {
            return true
        }

        return description.status != Description.Status.RUNNING && description.status != Description.Status.STARTING
    }
}
