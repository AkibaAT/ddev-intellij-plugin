package de.php_perfect.intellij.ddev.terminal

import com.intellij.execution.configurations.PtyCommandLine
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.terminal.pty.PtyProcessTtyConnector
import com.intellij.util.concurrency.AppExecutorUtil
import com.jediterm.terminal.TtyConnector
import com.pty4j.PtyProcess
import com.pty4j.unix.UnixPtyProcess
import de.php_perfect.intellij.ddev.cmd.wsl.WslAware
import de.php_perfect.intellij.ddev.state.DdevStateManager
import org.jetbrains.plugins.terminal.AbstractTerminalRunner
import org.jetbrains.plugins.terminal.ShellStartupOptions
import java.nio.charset.StandardCharsets
import java.util.Objects
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class DdevTerminalRunner(project: Project) : AbstractTerminalRunner<PtyProcess>(project) {
    override fun createProcess(startupOptions: ShellStartupOptions): PtyProcess {
        val ddevState = DdevStateManager.getInstance(myProject).state

        if (!ddevState.isAvailable) {
            throw ExecutionException("DDEV not installed", null)
        }

        val commandLine = PtyCommandLine(listOf(Objects.requireNonNull(ddevState.ddevBinary), "ssh"))
            .withConsoleMode(false)

        commandLine.setWorkDirectory(project.basePath)

        val patchedCommandLine = WslAware.patchCommandLine(commandLine)

        return try {
            patchedCommandLine.createProcess() as PtyProcess
        } catch (e: com.intellij.execution.ExecutionException) {
            throw ExecutionException("Opening DDEV Terminal failed", e)
        }
    }

    override fun createTtyConnector(process: PtyProcess): TtyConnector {
        return object : PtyProcessTtyConnector(process, StandardCharsets.UTF_8) {
            override fun close() {
                if (process is UnixPtyProcess) {
                    (process as UnixPtyProcess).hangup()
                    AppExecutorUtil.getAppScheduledExecutorService().schedule({
                        if (process.isAlive) {
                            LOG.info("Terminal hasn't been terminated by SIGHUP, performing default termination")
                            process.destroy()
                        }
                    }, 1000, TimeUnit.MILLISECONDS)
                } else {
                    process.destroy()
                }
            }
        }
    }

    override fun getDefaultTabTitle(): @NlsContexts.TabTitle String {
        return "DDEV Web Container"
    }

    override fun isTerminalSessionPersistent(): Boolean {
        return false
    }

    companion object {
        private val LOG = Logger.getInstance(DdevTerminalRunner::class.java)
    }
}
