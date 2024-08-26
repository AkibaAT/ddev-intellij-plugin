package de.php_perfect.intellij.ddev.cmd

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.version.Version

interface Ddev {
    @Throws(CommandFailedException::class)
    fun version(binary: String, project: Project): Version

    @Throws(CommandFailedException::class)
    fun detailedVersions(binary: String, project: Project): Versions

    @Throws(CommandFailedException::class)
    fun describe(binary: String, project: Project): Description

    companion object {
        @JvmStatic
        fun getInstance(): Ddev? {
            return ApplicationManager.getApplication().getService<Ddev?>(Ddev::class.java)
        }
    }
}
