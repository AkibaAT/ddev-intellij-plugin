package de.php_perfect.intellij.ddev.cmd

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project

class BinaryLocatorImpl : BinaryLocator {
    override fun findInPath(project: Project): String? {
        val projectDir = project.basePath
        val commandLine =
            GeneralCommandLine(WhichProvider.getWhichCommand(projectDir.toString()), DDEV_COMMAND).withWorkDirectory(projectDir)

        try {
            val processOutput = ProcessExecutor.getInstance()?.executeCommandLine(commandLine, COMMAND_TIMEOUT, true)

            if (processOutput?.exitCode != 0) {
                return null
            }

            return processOutput.stdout.trim()
        } catch (exception: ExecutionException) {
            LOG.error(exception)
            return null
        }
    }

    companion object {
        private val LOG = Logger.getInstance(BinaryLocatorImpl::class.java)

        private const val DDEV_COMMAND = "ddev"

        private const val COMMAND_TIMEOUT = 8000
    }
}
