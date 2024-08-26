package de.php_perfect.intellij.ddev.errorReporting

import com.intellij.diagnostic.IdeaReportingEvent
import com.intellij.execution.wsl.WSLDistribution
import com.intellij.ide.DataManager
import com.intellij.idea.IdeaLogger
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.util.Consumer
import de.php_perfect.intellij.ddev.DdevIntegrationBundle
import de.php_perfect.intellij.ddev.cmd.CommandFailedException
import de.php_perfect.intellij.ddev.cmd.Ddev.Companion.getInstance
import de.php_perfect.intellij.ddev.cmd.Versions
import de.php_perfect.intellij.ddev.notification.DdevNotifier
import de.php_perfect.intellij.ddev.state.DdevStateManager
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.protocol.SentryId
import java.awt.Component

class SentryErrorReporter : ErrorReportSubmitter() {
    override fun getReportActionText(): String {
        return DdevIntegrationBundle.message("errorReporting.submit")
    }

    override fun getPrivacyNoticeText(): String? {
        return DdevIntegrationBundle.message("errorReporting.privacyNotice")
    }

    override fun submit(
        events: Array<IdeaLoggingEvent>,
        additionalInfo: String?,
        parentComponent: Component,
        consumer: Consumer<in SubmittedReportInfo?>
    ): Boolean {
        val context = DataManager.getInstance().getDataContext(parentComponent)
        val project = CommonDataKeys.PROJECT.getData(context)

        ProgressManager.getInstance()
            .run(object : Task.Backgroundable(project, DdevIntegrationBundle.message("errorReporting.taskTitle")) {
                override fun run(indicator: ProgressIndicator) {
                    indicator.setIndeterminate(true)
                    val ddevVersions = getDdevVersions(project)
                    val wslDistribution = getWslDistribution(project)

                    for (event in events) {
                        val sentryId = captureIdeaLoggingEvent(event, additionalInfo, ddevVersions, wslDistribution)

                        if (project != null) {
                            DdevNotifier.getInstance(project).notifyErrorReportSent(sentryId.toString())
                        }
                    }

                    consumer.consume(SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE))
                }
            })

        return true
    }

    private fun captureIdeaLoggingEvent(
        event: IdeaLoggingEvent,
        additionalInfo: String?,
        ddevVersions: Versions?,
        wslDistribution: WSLDistribution?
    ): SentryId {
        return Sentry.captureEvent(buildSentryEvent(event, additionalInfo, ddevVersions, wslDistribution))
    }

    private fun buildSentryEvent(
        ideaLoggingEvent: IdeaLoggingEvent,
        additionalInfo: String?,
        ddevVersions: Versions?,
        wslDistribution: WSLDistribution?
    ): SentryEvent {
        val event = SentryEvent()
        event.setRelease(getPluginDescriptor().getVersion())

        if (additionalInfo != null) {
            event.setExtra("additional_info", additionalInfo)
        }

        event.setThrowable(ideaLoggingEvent.getThrowable())
        if (ideaLoggingEvent is IdeaReportingEvent) {
            event.setThrowable(ideaLoggingEvent.getData().getThrowable())
        }

        if (ddevVersions != null) {
            event.setTag(
                "ddev_version",
                (if (ddevVersions.getDdevVersion() != null) ddevVersions.getDdevVersion() else "")!!
            )
            event.setTag(
                "docker_version",
                (if (ddevVersions.getDockerVersion() != null) ddevVersions.getDockerVersion() else "")!!
            )
            event.setTag(
                "docker_platform",
                (if (ddevVersions.getDockerPlatform() != null) ddevVersions.getDockerPlatform() else "")!!
            )
            event.setTag(
                "docker_compose_version",
                (if (ddevVersions.getDockerComposeVersion() != null) ddevVersions.getDockerComposeVersion() else "")!!
            )
        }

        if (wslDistribution != null) {
            event.setTag("wsl_distribution", wslDistribution.getMsId())
        }

        event.setExtra("last_action_id", IdeaLogger.ourLastActionId)

        return event
    }

    private fun getDdevVersions(project: Project?): Versions? {
        if (project == null) {
            return null
        }

        val binary = DdevStateManager.getInstance(project).getState().getDdevBinary()

        if (binary == null) {
            return null
        }

        try {
            return getInstance()!!.detailedVersions(binary, project)
        } catch (e: CommandFailedException) {
            return null
        }
    }

    private fun getWslDistribution(project: Project?): WSLDistribution? {
        if (project == null || project.getBasePath() == null) {
            return null
        }

        return getDistributionByWindowsUncPath.getDistributionByWindowsUncPath(project.getBasePath())
    }
}
