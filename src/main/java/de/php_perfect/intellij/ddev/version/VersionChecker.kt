package de.php_perfect.intellij.ddev.version

import com.intellij.openapi.project.Project

interface VersionChecker {
    fun checkDdevVersion()

    fun checkDdevVersion(confirmNewestVersion: Boolean)

    companion object {
        fun getInstance(project: Project): VersionChecker {
            return project.getService<VersionChecker>(VersionChecker::class.java)
        }
    }
}
