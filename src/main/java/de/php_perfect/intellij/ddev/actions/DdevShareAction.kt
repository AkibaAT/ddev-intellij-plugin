package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.cmd.DdevRunner
import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.state.DdevStateManager
import de.php_perfect.intellij.ddev.state.State

class DdevShareAction : DdevRunAction() {
    override fun run(project: Project) {
        DdevRunner.getInstance().share(project)
    }

    override fun isActive(project: Project): Boolean {
        val state: State = DdevStateManager.getInstance(project).state

        if (!state.isAvailable) {
            return false
        }

        val description: Description? = state.description

        return description?.status == Description.Status.RUNNING
    }
}