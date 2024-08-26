package de.php_perfect.intellij.ddev.cmd

import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.application.ApplicationManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Exception

internal class DockerTest : BasePlatformTestCase() {
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
    fun dockerIsRunning() {
        val processOutput = ProcessOutput(0)

        val mockProcessExecutor = ApplicationManager.getApplication()
            .getService<ProcessExecutor?>(ProcessExecutor::class.java) as MockProcessExecutor
        mockProcessExecutor.addProcessOutput("docker info", processOutput)

        Assertions.assertTrue(DockerImpl().isRunning(this.getProject().getBasePath()))
    }

    @Test
    fun dockerIsNotRunning() {
        val processOutput = ProcessOutput(1)

        val mockProcessExecutor = ApplicationManager.getApplication()
            .getService<ProcessExecutor?>(ProcessExecutor::class.java) as MockProcessExecutor
        mockProcessExecutor.addProcessOutput("docker info", processOutput)

        Assertions.assertFalse(DockerImpl().isRunning(this.getProject().getBasePath()))
    }
}
