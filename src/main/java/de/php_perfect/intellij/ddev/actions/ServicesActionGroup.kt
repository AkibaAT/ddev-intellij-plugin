package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.serviceActions.ServiceActionManager
import de.php_perfect.intellij.ddev.state.DdevStateManager

class ServicesActionGroup : ActionGroup(), DumbAware {
    override fun getChildren(e: AnActionEvent?): Array<AnAction?> {
        if (e == null || e.project == null) {
            return arrayOfNulls<AnAction>(0)
        }

        return ServiceActionManager.getInstance(e.project!!)?.getServiceActions()!!
    }

    override fun update(e: AnActionEvent) {
        val project = e.project

        if (project == null) {
            e.presentation.isEnabled = false
            return
        }

        e.presentation.isEnabled = this.isActive(project)
    }

    private fun isActive(project: Project): Boolean {
        val state = DdevStateManager.getInstance(project).getState()
        val description = state.getDescription()

        if (description == null) {
            return false
        }

        return description.getStatus() == Description.Status.RUNNING
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
