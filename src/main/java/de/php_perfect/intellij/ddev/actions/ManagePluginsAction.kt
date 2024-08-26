package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction
import de.php_perfect.intellij.ddev.DdevIntegrationBundle
import javax.swing.Icon

class ManagePluginsAction : DumbAwareAction(
    DdevIntegrationBundle.messagePointer("action.DdevIntegration.ManagePlugins.text"),
    DdevIntegrationBundle.messagePointer("action.DdevIntegration.ManagePlugins.description"),
    null as Icon?
) {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.getProject()

        if (project == null) {
            return
        }

        ShowSettingsUtil.getInstance().showSettingsDialog(project, "Plugins")
    }
}
