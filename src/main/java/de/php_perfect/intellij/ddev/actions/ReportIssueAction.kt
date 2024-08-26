package de.php_perfect.intellij.ddev.actions

import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import de.php_perfect.intellij.ddev.DdevIntegrationBundle
import java.net.URI
import java.net.URISyntaxException

class ReportIssueAction : DumbAwareAction(
    DdevIntegrationBundle.messagePointer("action.DdevIntegration.ReportIssue.text"),
    DdevIntegrationBundle.messagePointer("action.DdevIntegration.ReportIssue.description"),
    AllIcons.Vcs.Vendors.Github
) {
    override fun actionPerformed(e: AnActionEvent) {
        try {
            BrowserUtil.browse(URI(ReportIssueAction.Companion.NEW_ISSUE_URL))
        } catch (ignored: URISyntaxException) {
        }
    }

    companion object {
        private const val NEW_ISSUE_URL =
            "https://github.com/php-perfect/ddev-intellij-plugin/issues/new?assignees=&labels=bug&template=bug_report.yml"
    }
}
