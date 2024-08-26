package de.php_perfect.intellij.ddev.cmd

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine

class DockerImpl : Docker {
    override fun isRunning(workDirectory: String?): Boolean {
        return try {
            ProcessExecutor.getInstance()?.executeCommandLine(
                GeneralCommandLine("docker", "info")
                    .withWorkDirectory(workDirectory),
                5000,
                false
            )?.exitCode == 0
        } catch (e: ExecutionException) {
            false
        }
    }

    override fun getContext(workDirectory: String?): String {
        return try {
            ProcessExecutor.getInstance()?.executeCommandLine(
                GeneralCommandLine("docker", "context", "show")
                    .withWorkDirectory(workDirectory),
                5000,
                false
            )?.stdout ?: ""
        } catch (e: ExecutionException) {
            ""
        }
    }
}
