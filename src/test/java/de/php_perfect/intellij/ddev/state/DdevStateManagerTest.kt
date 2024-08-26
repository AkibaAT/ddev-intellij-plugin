package de.php_perfect.intellij.ddev.state

import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.SystemInfo
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.cmd.MockProcessExecutor
import de.php_perfect.intellij.ddev.cmd.ProcessExecutor
import de.php_perfect.intellij.ddev.cmd.Service
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

internal class DdevStateManagerTest : BasePlatformTestCase() {
    @BeforeEach
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun testInitialize() {
        var expectedWhich = "which"
        if (SystemInfo.isWindows) {
            expectedWhich = "where"
        }

        val project = this.getProject()
        val ddevConfigLoader = DdevConfigLoader.getInstance(project) as MockDdevConfigLoader?
        val mockProcessExecutor = ApplicationManager.getApplication()
            .getService<ProcessExecutor?>(ProcessExecutor::class.java) as MockProcessExecutor
        mockProcessExecutor.addProcessOutput("docker info", ProcessOutput(0))
        mockProcessExecutor.addProcessOutput(
            expectedWhich + " ddev",
            ProcessOutput("/foo/bar/bin/ddev", "", 0, false, false)
        )

        ddevConfigLoader!!.setExists(true)
        this.prepareCommand("/foo/bar/bin/ddev --version", "ddev version v1.19.0")
        this.prepareCommandWithOutputFromFile(
            "/foo/bar/bin/ddev describe --json-output",
            "src/test/resources/ddev_describe.json"
        )

        val ddevStateManager = DdevStateManager.getInstance(project)
        ddevStateManager!!.initialize()

        val expectedState = StateImpl()
        expectedState.setDdevBinary("/foo/bar/bin/ddev")
        expectedState.setConfigured(true)
        expectedState.setDdevVersion(Version("v1.19.0"))
        expectedState.setDescription(
            Description(
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
        )

        Assertions.assertEquals(expectedState, ddevStateManager.getState())
    }

    @Test
    fun testReinitialize() {
        val project = this.getProject()

        val ddevConfigLoader = DdevConfigLoader.getInstance(project) as MockDdevConfigLoader?
        ddevConfigLoader!!.setExists(true)

        val ddevStateManager = DdevStateManager.getInstance(project)
        ddevStateManager!!.reinitialize()

        val expectedState = StateImpl()
        expectedState.setDdevBinary("")
        expectedState.setConfigured(true)
        expectedState.setDdevVersion(null)
        expectedState.setDescription(null)

        Assertions.assertEquals(expectedState, ddevStateManager.getState())
    }

    @Test
    fun testUpdateDescription() {
        var expectedWhich = "which"
        if (SystemInfo.isWindows) {
            expectedWhich = "where"
        }

        val project = this.getProject()
        val ddevConfigLoader = DdevConfigLoader.getInstance(project) as MockDdevConfigLoader?
        val mockProcessExecutor = ApplicationManager.getApplication()
            .getService<ProcessExecutor?>(ProcessExecutor::class.java) as MockProcessExecutor
        mockProcessExecutor.addProcessOutput("docker info", ProcessOutput(0))
        mockProcessExecutor.addProcessOutput(
            expectedWhich + " ddev",
            ProcessOutput("/foo/bar/bin/ddev", "", 0, false, false)
        )

        ddevConfigLoader!!.setExists(true)
        this.prepareCommand("/foo/bar/bin/ddev --version", "ddev version v1.19.0")
        this.prepareCommandWithOutputFromFile(
            "/foo/bar/bin/ddev describe --json-output",
            "src/test/resources/ddev_describe.json"
        )

        val ddevStateManager = DdevStateManager.getInstance(this.getProject())
        ddevStateManager!!.initialize()

        val expectedState = StateImpl()
        expectedState.setDdevBinary("/foo/bar/bin/ddev")
        expectedState.setConfigured(true)
        expectedState.setDdevVersion(Version("v1.19.0"))
        expectedState.setDescription(
            Description(
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
        )

        Assertions.assertEquals(expectedState, ddevStateManager.getState())

        this.prepareCommandWithOutputFromFile(
            "/foo/bar/bin/ddev describe --json-output",
            "src/test/resources/ddev_describe2.json"
        )

        ddevStateManager.updateDescription()

        expectedState.setDescription(
            Description(
                "acol",
                "7.4",
                Description.Status.STOPPED,
                null,
                null,
                null,
                null,
                HashMap<String?, Service?>(),
                null,
                "https://acol.ddev.site"
            )
        )

        Assertions.assertEquals(expectedState, ddevStateManager.getState())
    }

    private fun prepareCommand(command: String, output: String) {
        val mockProcessExecutor = ApplicationManager.getApplication()
            .getService<ProcessExecutor?>(ProcessExecutor::class.java) as MockProcessExecutor

        val processOutput = ProcessOutput(output, "", 0, false, false)
        mockProcessExecutor.addProcessOutput(command, processOutput)
    }

    private fun prepareCommandWithOutputFromFile(command: String, file: String) {
        try {
            this.prepareCommand(command, Files.readString(Path.of(file)))
        } catch (e: IOException) {
            Assertions.fail<Any?>(e)
        }
    }

    @AfterEach
    @Throws(Exception::class)
    override fun tearDown() {
        val ddevConfigLoader = DdevConfigLoader.getInstance(this.getProject()) as MockDdevConfigLoader?
        ddevConfigLoader!!.setExists(false)

        DdevStateManager.getInstance(this.getProject())!!.resetState()

        super.tearDown()
    }
}
