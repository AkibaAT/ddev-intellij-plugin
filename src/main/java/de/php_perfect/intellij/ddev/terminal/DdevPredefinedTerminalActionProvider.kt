package de.php_perfect.intellij.ddev.terminal

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.state.DdevStateManager.Companion.getInstance
import org.jetbrains.plugins.terminal.ui.OpenPredefinedTerminalActionProvider

class DdevPredefinedTerminalActionProvider : OpenPredefinedTerminalActionProvider {
    override fun listOpenPredefinedTerminalActions(project: Project): List<AnAction> {
        val state = getInstance(project)!!.getState()

        if (!state.isAvailable() || !state.isConfigured()) {
            return mutableListOf<AnAction?>() as List<AnAction>
        }

        return listOf<AnAction?>(ActionManager.getInstance().getAction("DdevIntegration.Terminal")) as List<AnAction>
    }
}
