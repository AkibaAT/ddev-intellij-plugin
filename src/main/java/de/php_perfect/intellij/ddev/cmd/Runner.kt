package de.php_perfect.intellij.ddev.cmd

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project

interface Runner {
    fun run(commandLine: GeneralCommandLine, title: String)

    fun run(commandLine: GeneralCommandLine, title: String, afterCompletion: Runnable?)

    companion object {
        fun getInstance(project: Project): Runner? {
            return project.getService<Runner?>(Runner::class.java)
        }
    }
}
