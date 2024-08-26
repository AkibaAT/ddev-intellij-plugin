package de.php_perfect.intellij.ddev.cmd

import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.SystemInfo
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Exception

internal class BinaryLocatorTest : BasePlatformTestCase() {
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
    fun findBinary() {
        var expectedWhich = "which"
        if (SystemInfo.isWindows) {
            expectedWhich = "where"
        }

        val processOutput = ProcessOutput("/foo/bar/bin/ddev", "", 0, false, false)

        val mockProcessExecutor = ApplicationManager.getApplication()
            .getService<ProcessExecutor?>(ProcessExecutor::class.java) as MockProcessExecutor
        mockProcessExecutor.addProcessOutput(expectedWhich + " ddev", processOutput)

        Assertions.assertEquals("/foo/bar/bin/ddev", BinaryLocatorImpl().findInPath(getProject()))
    }

    @Test
    fun isNotInstalled() {
        var expectedWhich = "which"
        if (SystemInfo.isWindows) {
            expectedWhich = "where"
        }

        val processOutput = ProcessOutput("", "", 1, false, false)

        val mockProcessExecutor = ApplicationManager.getApplication()
            .getService<ProcessExecutor?>(ProcessExecutor::class.java) as MockProcessExecutor
        mockProcessExecutor.addProcessOutput(expectedWhich + " ddev", processOutput)

        Assertions.assertNull(BinaryLocatorImpl().findInPath(getProject()))
    }
}
