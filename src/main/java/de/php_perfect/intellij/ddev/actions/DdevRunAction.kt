package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

abstract class DdevRunAction : DdevAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        run(project)
    }

    protected abstract fun run(project: Project)

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
