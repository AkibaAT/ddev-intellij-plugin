package de.php_perfect.intellij.ddev.version

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicator

interface ReleaseClient {
    fun loadCurrentVersion(indicator: ProgressIndicator): LatestRelease?

    companion object {
        fun getInstance(): ReleaseClient {
            return ApplicationManager.getApplication().getService<ReleaseClient>(ReleaseClient::class.java)
        }
    }
}
