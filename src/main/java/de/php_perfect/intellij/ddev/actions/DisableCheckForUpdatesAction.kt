package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import de.php_perfect.intellij.ddev.DdevIntegrationBundle
import de.php_perfect.intellij.ddev.settings.DdevSettingsState

class DisableCheckForUpdatesAction :
    DumbAwareAction(DdevIntegrationBundle.messagePointer("actions.disableCheckForUpdates")) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.getProject()

        if (project == null) {
            return
        }

        DdevSettingsState.getInstance(project).checkForUpdates = false
    }
}
