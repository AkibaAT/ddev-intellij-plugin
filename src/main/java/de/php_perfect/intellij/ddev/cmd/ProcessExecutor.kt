package de.php_perfect.intellij.ddev.cmd

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.application.ApplicationManager

interface ProcessExecutor {
    @Throws(ExecutionException::class)
    fun executeCommandLine(commandLine: GeneralCommandLine?, timeout: Int, loginShell: Boolean): ProcessOutput

    companion object {
        fun getInstance(): ProcessExecutor? {
            return ApplicationManager.getApplication().getService<ProcessExecutor?>(ProcessExecutor::class.java)
        }
    }
}
