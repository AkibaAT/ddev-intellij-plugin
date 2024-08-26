package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbAwareAction
import de.php_perfect.intellij.ddev.state.DdevStateManager

class SyncStateAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.getProject()

        if (project == null) {
            return
        }

        ApplicationManager.getApplication()
            .executeOnPooledThread(Runnable { DdevStateManager.getInstance(project).updateDescription() })
    }
}
