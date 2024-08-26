package de.php_perfect.intellij.ddev.cmd

import com.intellij.execution.wsl.WSLDistribution
import com.intellij.openapi.util.SystemInfo

internal object WhichProvider {
    fun getWhichCommand(workingDirectory: String): String {
        if (!SystemInfo.isWindows) {
            return "which"
        }

        val distribution: WSLDistribution? =
            getDistributionByWindowsUncPath.getDistributionByWindowsUncPath(workingDirectory)

        if (distribution != null) {
            return "which"
        }

        return "where"
    }
}
