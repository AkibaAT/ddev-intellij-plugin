package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.cmd.DdevRunner
import de.php_perfect.intellij.ddev.state.DdevStateManager
import de.php_perfect.intellij.ddev.state.State

class DdevConfigAction : DdevRunAction() {
    override fun run(project: Project) {
        DdevRunner.getInstance().config(project)
    }

    override fun isActive(project: Project): Boolean {
        val state: State = DdevStateManager.getInstance(project).state
        return state.isAvailable && !state.isConfigured
    }
}
