package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction
import de.php_perfect.intellij.ddev.DdevIntegrationBundle
import de.php_perfect.intellij.ddev.settings.DdevSettingsConfigurable
import javax.swing.Icon

class ChangeSettingsAction : DumbAwareAction(
    DdevIntegrationBundle.messagePointer("action.DdevIntegration.ChangeSettings.text"),
    DdevIntegrationBundle.messagePointer("action.DdevIntegration.ChangeSettings.description"),
    null as Icon?
) {
    override fun actionPerformed(e: AnActionEvent) {
        ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), DdevSettingsConfigurable.getName())
    }
}
