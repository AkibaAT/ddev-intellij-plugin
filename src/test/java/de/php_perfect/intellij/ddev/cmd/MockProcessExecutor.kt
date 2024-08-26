package de.php_perfect.intellij.ddev.cmd

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.util.SystemInfo
import java.util.HashMap

class MockProcessExecutor : ProcessExecutor {
    private val processList: MutableMap<String?, ProcessOutput> = HashMap<String?, ProcessOutput>()

    init {
        this.addProcessOutput("docker info", ProcessOutput(0))

        if (SystemInfo.isWindows) {
            this.addProcessOutput("where ddev", ProcessOutput(1))
        } else {
            this.addProcessOutput("which ddev", ProcessOutput(1))
        }
    }

    fun addProcessOutput(command: String, processOutput: ProcessOutput) {
        this.processList.put(command, processOutput)
    }

    @Throws(ExecutionException::class)
    override fun executeCommandLine(commandLine: GeneralCommandLine, timeout: Int, loginShell: Boolean): ProcessOutput {
        val commandLineString = commandLine.getCommandLineString()

        if (!this.processList.containsKey(commandLineString)) {
            throw ExecutionException(String.format("[TEST] Command '%s' was not expected", commandLineString))
        }

        val processOutput: ProcessOutput = this.processList.get(commandLineString)!!
        this.processList.remove(commandLineString)
        return processOutput
    }
}
