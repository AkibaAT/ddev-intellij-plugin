package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.state.DdevStateManager
import de.php_perfect.intellij.ddev.state.State
import de.php_perfect.intellij.ddev.terminal.DdevTerminalRunner
import org.jetbrains.plugins.terminal.TerminalTabState
import org.jetbrains.plugins.terminal.TerminalToolWindowManager

class DdevPredefinedTerminalAction : DdevAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val runner = DdevTerminalRunner(project)
        val tabState = TerminalTabState()
        tabState.myTabName = "DDEV Web Container"

        TerminalToolWindowManager.getInstance(project).createNewSession(runner, tabState)
    }

    override fun isActive(project: Project): Boolean {
        val state = DdevStateManager.getInstance(project).state

        if (!state.isAvailable || !state.isConfigured) {
            return false
        }

        val description = state.description ?: return false

        return description.status == Description.Status.RUNNING
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
