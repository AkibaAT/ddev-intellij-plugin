package de.php_perfect.intellij.ddev.cmd

import com.google.common.util.concurrent.UncheckedExecutionException
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.EmptyProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import de.php_perfect.intellij.ddev.cmd.wsl.WslAware.patchCommandLine
import java.util.concurrent.atomic.AtomicReference

class ProcessExecutorImpl : ProcessExecutor {
    @Throws(ExecutionException::class)
    override fun executeCommandLine(
        commandLine: GeneralCommandLine?,
        timeout: Int,
        loginShell: Boolean
    ): ProcessOutput {
        val patchedCommandLine = patchCommandLine<GeneralCommandLine?>(commandLine, loginShell)
        val outputReference = AtomicReference<ProcessOutput>()

        ProgressManager.getInstance().runProcess(Runnable {
            try {
                val processHandler = CapturingProcessHandler(patchedCommandLine!!)
                val output = processHandler.runProcess(timeout)
                outputReference.set(output)

                LOG.debug("command: " + processHandler.getCommandLine() + " returned: " + output)
            } catch (e: ExecutionException) {
                throw UncheckedExecutionException(e)
            }
        }, EmptyProgressIndicator())

        return outputReference.get()
    }

    companion object {
        val LOG: Logger = Logger.getInstance(ProcessExecutorImpl::class.java)
    }
}
