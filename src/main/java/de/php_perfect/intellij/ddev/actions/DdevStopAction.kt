package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.cmd.DdevRunner
import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.state.DdevStateManager

class DdevStopAction : DdevRunAction() {
    override fun run(project: Project) {
        DdevRunner.getInstance()?.stop(project)
    }

    override fun isActive(project: Project): Boolean {
        val state = DdevStateManager.getInstance(project).getState()

        if (!state.isAvailable() || !state.isConfigured()) {
            return false
        }

        val description = state.getDescription()

        if (description == null) {
            return true
        }

        return description.getStatus() == Description.Status.RUNNING
    }
}
