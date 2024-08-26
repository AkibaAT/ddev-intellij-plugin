package de.php_perfect.intellij.ddev.terminal

import com.intellij.execution.configurations.PtyCommandLine
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts.TabTitle
import com.intellij.terminal.pty.PtyProcessTtyConnector
import com.intellij.util.concurrency.AppExecutorUtil
import com.jediterm.terminal.TtyConnector
import com.pty4j.PtyProcess
import com.pty4j.unix.UnixPtyProcess
import de.php_perfect.intellij.ddev.cmd.wsl.WslAware.patchCommandLine
import de.php_perfect.intellij.ddev.state.DdevStateManager.Companion.getInstance
import org.jetbrains.plugins.terminal.AbstractTerminalRunner
import org.jetbrains.plugins.terminal.ShellStartupOptions
import java.nio.charset.StandardCharsets
import java.util.List
import java.util.Objects
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class DdevTerminalRunner(project: Project) : AbstractTerminalRunner<PtyProcess?>(project) {
    @Throws(ExecutionException::class)
    override fun createProcess(startupOptions: ShellStartupOptions): PtyProcess {
        val ddevState = getInstance(this.myProject)!!.getState()

        if (!ddevState.isAvailable()) {
            throw ExecutionException("DDEV not installed", null)
        }

        val commandLine =
            PtyCommandLine(List.of<String?>(Objects.requireNonNull<String?>(ddevState.getDdevBinary()), "ssh"))
                .withConsoleMode(false)

        commandLine.setWorkDirectory(getProject().getBasePath())

        val patchedCommandLine = patchCommandLine<PtyCommandLine?>(commandLine)

        try {
            return patchedCommandLine!!.createProcess() as PtyProcess
        } catch (e: com.intellij.execution.ExecutionException) {
            throw ExecutionException("Opening DDEV Terminal failed", e)
        }
    }

    override fun createTtyConnector(process: PtyProcess): TtyConnector {
        return object : PtyProcessTtyConnector(process, StandardCharsets.UTF_8) {
            override fun close() {
                if (process is UnixPtyProcess) {
                    process.hangup()
                    AppExecutorUtil.getAppScheduledExecutorService().schedule(Runnable {
                        if (process.isAlive()) {
                            DdevTerminalRunner.Companion.LOG.info("Terminal hasn't been terminated by SIGHUP, performing default termination")
                            process.destroy()
                        }
                    }, 1000, TimeUnit.MILLISECONDS)
                } else {
                    process.destroy()
                }
            }
        }
    }

    override fun getDefaultTabTitle(): @TabTitle String {
        return "DDEV Web Container"
    }

    override fun isTerminalSessionPersistent(): Boolean {
        return false
    }

    companion object {
        private val LOG = Logger.getInstance(DdevTerminalRunner::class.java)
    }
}
