package de.php_perfect.intellij.ddev.version

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.notification.DdevNotifier
import de.php_perfect.intellij.ddev.notification.DdevNotifier.Companion.getInstance
import de.php_perfect.intellij.ddev.settings.DdevSettingsState
import de.php_perfect.intellij.ddev.settings.DdevSettingsState.Companion.getInstance
import de.php_perfect.intellij.ddev.state.DdevStateManager
import de.php_perfect.intellij.ddev.state.DdevStateManager.Companion.getInstance
import de.php_perfect.intellij.ddev.state.State
import de.php_perfect.intellij.ddev.version.util.VersionCompare

class VersionCheckerImpl(project: Project) : VersionChecker {
    private val project: Project

    init {
        this.project = project
    }

    override fun checkDdevVersion() {
        this.checkDdevVersion(false)
    }

    override fun checkDdevVersion(confirmNewestVersion: Boolean) {
        val settings = DdevSettingsState.getInstance(this.project)

        if (!settings.checkForUpdates) {
            return
        }

        val state = DdevStateManager.getInstance(this.project)!!.getState()
        val currentVersion = this.getCurrentVersion(state)

        if (currentVersion == null) {
            if (state.isConfigured()) {
                DdevNotifier.getInstance(project)!!.notifyInstallDdev()
            }
            return
        }

        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Checking DDEV version", true) {
            override fun run(progressIndicator: ProgressIndicator) {
                val latestRelease: LatestRelease? =
                    ReleaseClient.Companion.getInstance().loadCurrentVersion(progressIndicator)
                progressIndicator.checkCanceled()

                if (latestRelease == null || latestRelease.getTagName() == null) {
                    return
                }

                val latestVersion = Version(latestRelease.getTagName()!!)

                if (VersionCompare.needsUpdate(currentVersion, latestVersion)) {
                    DdevNotifier.getInstance(project)!!
                        .notifyNewVersionAvailable(currentVersion.toString(), latestVersion.toString())
                } else if (confirmNewestVersion) {
                    DdevNotifier.getInstance(project)!!.notifyAlreadyLatestVersion()
                }
            }
        })
    }

    private fun getCurrentVersion(state: State): Version? {
        if (!state.isAvailable()) {
            return null
        }

        return state.getDdevVersion()
    }
}
