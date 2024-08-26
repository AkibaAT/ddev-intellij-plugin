package de.php_perfect.intellij.ddev.cmd

import com.intellij.execution.ExecutionException
import com.intellij.execution.RunContentExecutor
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Computable
import com.intellij.openapi.util.Disposer
import de.php_perfect.intellij.ddev.cmd.wsl.WslAware.patchCommandLine

class RunnerImpl(project: Project) : Runner, Disposable {
    private val project: Project

    init {
        this.project = project
    }

    override fun run(commandLine: GeneralCommandLine, title: String) {
        this.run(commandLine, title, null)
    }

    override fun run(commandLine: GeneralCommandLine, title: String, afterCompletion: Runnable?) {
        ApplicationManager.getApplication().invokeLater(Runnable {
            try {
                val processHandler = this.createProcessHandler(commandLine)
                val runContentExecutor = RunContentExecutor(this.project, processHandler)
                    .withTitle(title)
                    .withActivateToolWindow(true)
                    .withAfterCompletion(afterCompletion)
                    .withStop(
                        Runnable { processHandler.destroyProcess() },
                        Computable { !processHandler.isProcessTerminated() })
                Disposer.register(this, runContentExecutor)
                runContentExecutor.run()
            } catch (exception: ExecutionException) {
                LOG.warn("An error occurred running " + commandLine.getCommandLineString(), exception)
            }
        }, ModalityState.nonModal())
    }

    @Throws(ExecutionException::class)
    private fun createProcessHandler(commandLine: GeneralCommandLine?): ProcessHandler {
        val handler: ProcessHandler = ColoredProcessHandler(patchCommandLine<GeneralCommandLine?>(commandLine)!!)
        ProcessTerminatedListener.attach(handler)

        return handler
    }

    override fun dispose() {
        // Use service as parent disposable for running processes
    }

    companion object {
        private val LOG = Logger.getInstance(RunnerImpl::class.java)
    }
}
