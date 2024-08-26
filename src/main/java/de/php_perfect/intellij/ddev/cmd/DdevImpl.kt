package de.php_perfect.intellij.ddev.cmd

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.cmd.parser.JsonParser.Companion.getInstance
import de.php_perfect.intellij.ddev.cmd.parser.JsonParserException
import de.php_perfect.intellij.ddev.version.Version
import org.apache.commons.compress.utils.Lists
import java.lang.reflect.Type
import java.util.regex.Pattern

class DdevImpl : Ddev {
    @Throws(CommandFailedException::class)
    override fun version(binary: String, project: Project): Version {
        val versionString = this.executePlain(binary, "--version", project)
        val r = Pattern.compile("ddev version (v.*)$")
        val m = r.matcher(versionString)

        if (m.find()) {
            return Version(m.group(1))
        }

        throw CommandFailedException("Unexpected output of ddev version command: $versionString")
    }

    @Throws(CommandFailedException::class)
    override fun detailedVersions(binary: String, project: Project): Versions {
        return execute<Versions>(binary, "version", Versions::class.java, project)
    }

    @Throws(CommandFailedException::class)
    override fun describe(binary: String, project: Project): Description {
        return execute<Description>(binary, "describe", Description::class.java, project)
    }

    @Throws(CommandFailedException::class)
    private fun executePlain(binary: String, action: String, project: Project): String {
        val commandLine = createDdevCommandLine(binary, action, project, false)

        try {
            val processOutput = ProcessExecutor.getInstance()?.executeCommandLine(commandLine, COMMAND_TIMEOUT, false)

            if (processOutput?.isTimeout == true) {
                throw CommandFailedException(
                    "Command timed out after " + (COMMAND_TIMEOUT / 1000) + " seconds: " + commandLine.commandLineString + " in " + commandLine.workDirectory
                        .path
                )
            }

            if (processOutput?.exitCode != 0) {
                throw CommandFailedException("Command '" + commandLine.commandLineString + "' returned non zero exit code " + processOutput)
            }

            return processOutput.stdout
        } catch (exception: ExecutionException) {
            throw CommandFailedException("Failed to execute " + commandLine.commandLineString, exception)
        }
    }

    @Throws(CommandFailedException::class)
    private fun <T> execute(binary: String, action: String, type: Type, project: Project): T {
        val commandLine = createDdevCommandLine(binary, action, project)

        var processOutput: ProcessOutput? = null
        try {
            processOutput = ProcessExecutor.getInstance()?.executeCommandLine(commandLine, COMMAND_TIMEOUT, false)

            if (processOutput?.isTimeout == true) {
                throw CommandFailedException(
                    "Command timed out after " + (COMMAND_TIMEOUT / 1000) + " seconds: " + commandLine.commandLineString + " in " + commandLine.workDirectory
                        .path
                )
            }

            if (processOutput?.exitCode != 0) {
                throw CommandFailedException("Command '" + commandLine.commandLineString + "' returned non zero exit code " + processOutput)
            }

            return getInstance().parse<T>(processOutput.stdout, type)
        } catch (exception: ExecutionException) {
            throw CommandFailedException("Failed to execute " + commandLine.commandLineString, exception)
        } catch (exception: JsonParserException) {
            throw CommandFailedException(
                "Failed to parse output of command '" + commandLine.commandLineString + "': " + processOutput!!.stdout,
                exception
            )
        }
    }

    private fun createDdevCommandLine(
        binary: String,
        action: String,
        project: Project,
        json: Boolean = true
    ): GeneralCommandLine {
        val arguments = Lists.newArrayList<String?>()
        arguments.add(binary)
        arguments.add(action)

        if (json) {
            arguments.add("--json-output")
        }

        return GeneralCommandLine(arguments)
            .withWorkDirectory(project.basePath)
            .withEnvironment("DDEV_NONINTERACTIVE", "true")
    }

    companion object {
        private const val COMMAND_TIMEOUT = 8000
    }
}
