package de.php_perfect.intellij.ddev.notification

import com.intellij.openapi.project.Project

interface DdevNotifier {
    fun notifyInstallDdev()

    fun notifyNewVersionAvailable(currentVersion: String, newVersion: String)

    fun notifyAlreadyLatestVersion()

    fun notifyMissingPlugin(pluginName: String)

    fun notifyPhpInterpreterUpdated(phpVersion: String)

    fun notifyUnknownStateEntered()

    fun notifyErrorReportSent(id: String)

    fun notifyDdevDetected(binary: String)

    fun notifyDockerNotAvailable(context: String)

    companion object {
        @JvmStatic
        fun getInstance(project: Project): DdevNotifier? {
            return project.getService<DdevNotifier?>(DdevNotifier::class.java)
        }
    }
}
