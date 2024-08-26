package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.cmd.DdevRunner
import de.php_perfect.intellij.ddev.state.DdevStateManager

class DdevConfigAction : DdevRunAction() {
    override fun run(project: Project) {
        DdevRunner.getInstance()?.config(project)
    }

    override fun isActive(project: Project): Boolean {
        val state = DdevStateManager.getInstance(project).getState()

        return state.isAvailable() && !state.isConfigured()
    }
}
