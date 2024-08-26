package de.php_perfect.intellij.ddev.cmd

import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.application.ApplicationManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import de.php_perfect.intellij.ddev.version.Version
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.util.HashMap

internal class DdevImplTest : BasePlatformTestCase() {
    @BeforeEach
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
    }

    @AfterEach
    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
    }

    @Test
    @Throws(CommandFailedException::class)
    fun version() {
        val expected = Version("v1.22.0")
        val processOutput = ProcessOutput("ddev version v1.22.0", "", 0, false, false)

        val mockProcessExecutor = ApplicationManager.getApplication()
            .getService<ProcessExecutor?>(ProcessExecutor::class.java) as MockProcessExecutor
        mockProcessExecutor.addProcessOutput("ddev --version", processOutput)

        Assertions.assertEquals(expected, DdevImpl().version("ddev", getProject()))
    }

    @Test
    @Throws(CommandFailedException::class, IOException::class)
    fun detailedVersions() {
        val expected = Versions("v1.19.0", "20.10.12", "v2.2.2", "docker-desktop")

        val processOutput =
            ProcessOutput(Files.readString(Path.of("src/test/resources/ddev_version.json")), "", 0, false, false)

        val mockProcessExecutor = ApplicationManager.getApplication()
            .getService<ProcessExecutor?>(ProcessExecutor::class.java) as MockProcessExecutor
        mockProcessExecutor.addProcessOutput("ddev version --json-output", processOutput)

        Assertions.assertEquals(expected, DdevImpl().detailedVersions("ddev", getProject()))
    }

    @Test
    @Throws(CommandFailedException::class, IOException::class)
    fun describe() {
        val expected = Description(
            "acol",
            "8.1",
            Description.Status.STOPPED,
            null,
            null,
            null,
            null,
            HashMap<String?, Service?>(),
            null,
            "https://acol.ddev.site"
        )

        val processOutput =
            ProcessOutput(Files.readString(Path.of("src/test/resources/ddev_describe.json")), "", 0, false, false)

        val mockProcessExecutor = ApplicationManager.getApplication()
            .getService<ProcessExecutor?>(ProcessExecutor::class.java) as MockProcessExecutor
        mockProcessExecutor.addProcessOutput("ddev describe --json-output", processOutput)

        Assertions.assertEquals(expected, DdevImpl().describe("ddev", getProject()))
    }
}
