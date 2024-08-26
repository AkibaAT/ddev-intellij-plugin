package de.php_perfect.intellij.ddev.notification

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.DdevIntegrationBundle
import de.php_perfect.intellij.ddev.actions.ChangeSettingsAction
import de.php_perfect.intellij.ddev.actions.DisableCheckForUpdatesAction
import de.php_perfect.intellij.ddev.actions.InstallationInstructionsAction
import de.php_perfect.intellij.ddev.actions.ManagePluginsAction
import de.php_perfect.intellij.ddev.actions.ReloadPluginAction
import de.php_perfect.intellij.ddev.actions.ReportIssueAction

class DdevNotifierImpl(project: Project) : DdevNotifier {
    private val project: Project

    init {
        this.project = project
    }

    override fun notifyInstallDdev() {
        ApplicationManager.getApplication().invokeLater(Runnable {
            NotificationGroupManager.getInstance()
                .getNotificationGroup(STICKY)
                .createNotification(
                    DdevIntegrationBundle.message("notification.InstallDdev.title"),
                    DdevIntegrationBundle.message("notification.InstallDdev.text"),
                    NotificationType.INFORMATION
                )
                .addAction(InstallationInstructionsAction())
                .notify(this.project)
        }, ModalityState.nonModal())
    }

    override fun notifyNewVersionAvailable(currentVersion: String, latestVersion: String) {
        ApplicationManager.getApplication().invokeLater(Runnable {
            NotificationGroupManager.getInstance()
                .getNotificationGroup(NON_STICKY)
                .createNotification(
                    DdevIntegrationBundle.message("notification.NewVersionAvailable.title"),
                    DdevIntegrationBundle.message(
                        "notification.NewVersionAvailable.text",
                        currentVersion,
                        latestVersion
                    ),
                    NotificationType.INFORMATION
                )
                .addAction(InstallationInstructionsAction())
                .addAction(DisableCheckForUpdatesAction())
                .notify(this.project)
        }, ModalityState.nonModal())
    }

    override fun notifyAlreadyLatestVersion() {
        ApplicationManager.getApplication().invokeLater(Runnable {
            NotificationGroupManager.getInstance()
                .getNotificationGroup(NON_STICKY)
                .createNotification(
                    DdevIntegrationBundle.message("notification.AlreadyLatestVersion.text"),
                    NotificationType.INFORMATION
                )
                .notify(this.project)
        }, ModalityState.nonModal())
    }

    override fun notifyMissingPlugin(pluginName: String) {
        ApplicationManager.getApplication().invokeLater(Runnable {
            NotificationGroupManager.getInstance()
                .getNotificationGroup(STICKY)
                .createNotification(
                    DdevIntegrationBundle.message("notification.MissingPlugin.title"),
                    DdevIntegrationBundle.message("notification.MissingPlugin.text", pluginName),
                    NotificationType.WARNING
                )
                .addAction(ManagePluginsAction())
                .notify(this.project)
        }, ModalityState.nonModal())
    }

    override fun notifyPhpInterpreterUpdated(phpVersion: String) {
        ApplicationManager.getApplication().invokeLater(Runnable {
            NotificationGroupManager.getInstance()
                .getNotificationGroup(NON_STICKY)
                .createNotification(
                    DdevIntegrationBundle.message("notification.InterpreterUpdated.title"),
                    DdevIntegrationBundle.message("notification.InterpreterUpdated.text", phpVersion),
                    NotificationType.INFORMATION
                )
                .notify(this.project)
        }, ModalityState.nonModal())
    }

    override fun notifyUnknownStateEntered() {
        ApplicationManager.getApplication().invokeLater(Runnable {
            NotificationGroupManager.getInstance()
                .getNotificationGroup(STICKY)
                .createNotification(
                    DdevIntegrationBundle.message("notification.UnknownStateEntered.title"),
                    DdevIntegrationBundle.message("notification.UnknownStateEntered.text"),
                    NotificationType.WARNING
                )
                .addAction(ActionManager.getInstance().getAction("DdevIntegration.SyncState"))
                .addAction(ReportIssueAction())
                .notify(this.project)
        }, ModalityState.nonModal())
    }

    override fun notifyErrorReportSent(reportId: String) {
        ApplicationManager.getApplication().invokeLater(Runnable {
            NotificationGroupManager.getInstance()
                .getNotificationGroup(NON_STICKY)
                .createNotification(
                    DdevIntegrationBundle.message("errorReporting.success.title"),
                    DdevIntegrationBundle.message("errorReporting.success.text", reportId),
                    NotificationType.INFORMATION
                )
                .addAction(ReportIssueAction())
                .notify(this.project)
        }, ModalityState.nonModal())
    }

    override fun notifyDdevDetected(binary: String) {
        ApplicationManager.getApplication().invokeLater(Runnable {
            NotificationGroupManager.getInstance()
                .getNotificationGroup(NON_STICKY)
                .createNotification(
                    DdevIntegrationBundle.message("notification.ddevDetected.title"),
                    DdevIntegrationBundle.message("notification.ddevDetected.text", binary),
                    NotificationType.INFORMATION
                )
                .addAction(ChangeSettingsAction())
                .notify(this.project)
        }, ModalityState.nonModal())
    }

    override fun notifyDockerNotAvailable(context: String) {
        ApplicationManager.getApplication().invokeLater(Runnable {
            NotificationGroupManager.getInstance()
                .getNotificationGroup(STICKY)
                .createNotification(
                    DdevIntegrationBundle.message("notification.dockerNotAvailable.title"),
                    DdevIntegrationBundle.message("notification.dockerNotAvailable.text", context),
                    NotificationType.WARNING
                )
                .addAction(ReloadPluginAction())
                .notify(this.project)
        }, ModalityState.nonModal())
    }

    companion object {
        const val STICKY: String = "DdevIntegration.Sticky"
        const val NON_STICKY: String = "DdevIntegration.NonSticky"
    }
}
