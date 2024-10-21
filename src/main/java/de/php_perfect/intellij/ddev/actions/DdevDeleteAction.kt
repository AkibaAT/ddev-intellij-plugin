package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.cmd.DdevRunner
import de.php_perfect.intellij.ddev.state.DdevStateManager
import de.php_perfect.intellij.ddev.state.State
import org.jetbrains.annotations.NotNull

class DdevDeleteAction : DdevRunAction() {
    override fun run(@NotNull project: Project) {
        DdevRunner.getInstance().delete(project)
    }

    override fun isActive(@NotNull project: Project): Boolean {
        val state: State = DdevStateManager.getInstance(project).state
        return state.isAvailable && state.isConfigured
    }
}
