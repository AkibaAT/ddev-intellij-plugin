package de.php_perfect.intellij.ddev.cmd

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project

interface DdevRunner {
    fun start(project: Project)

    fun restart(project: Project)

    fun stop(project: Project)

    fun powerOff(project: Project)

    fun delete(project: Project)

    fun share(project: Project)

    fun config(project: Project)

    companion object {
        @JvmStatic
        fun getInstance(): DdevRunner? {
            return ApplicationManager.getApplication().getService<DdevRunner?>(DdevRunner::class.java)
        }
    }
}
