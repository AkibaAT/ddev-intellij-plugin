package de.php_perfect.intellij.ddev.actions

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsActions
import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.state.DdevStateManager
import java.net.URL
import java.util.Objects
import javax.swing.Icon

class OpenServiceAction(
    private val url: URL, text: @NlsActions.ActionText String,
    description: @NlsActions.ActionDescription String?, icon: Icon?
) : DdevAwareAction(text, description, icon) {

    override fun actionPerformed(e: AnActionEvent) {
        BrowserUtil.browse(this.url)
    }

    override fun isActive(project: Project): Boolean {
        val state = DdevStateManager.getInstance(project).getState()

        if (!state.isAvailable() || !state.isConfigured()) {
            return false
        }

        val description = state.getDescription()

        if (description == null) {
            return false
        }

        return description.getStatus() == Description.Status.RUNNING
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as OpenServiceAction
        return url == that.url
    }

    override fun hashCode(): Int {
        return Objects.hash(url)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
