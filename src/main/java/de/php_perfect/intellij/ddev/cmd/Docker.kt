package de.php_perfect.intellij.ddev.cmd

import com.intellij.openapi.application.ApplicationManager

interface Docker {
    fun isRunning(workDirectory: String?): Boolean

    fun getContext(workDirectory: String?): String

    companion object {
        @JvmStatic
        fun getInstance(): Docker? {
            return ApplicationManager.getApplication().getService<Docker?>(Docker::class.java)
        }
    }
}
