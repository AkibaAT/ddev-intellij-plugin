package de.php_perfect.intellij.ddev.cmd

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project

interface BinaryLocator {
    fun findInPath(project: Project): String?

    companion object {
        @JvmStatic
        fun getInstance(): BinaryLocator? {
            return ApplicationManager.getApplication().getService<BinaryLocator?>(BinaryLocator::class.java)
        }
    }
}
