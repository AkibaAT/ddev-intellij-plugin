package de.php_perfect.intellij.ddev.version.util

import de.php_perfect.intellij.ddev.version.Version

object VersionCompare {
    @JvmStatic
    fun needsUpdate(currentVersion: Version, latestVersion: Version): Boolean {
        return currentVersion.compareTo(latestVersion) < 0
    }
}
