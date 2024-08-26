package de.php_perfect.intellij.ddev.terminal

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Exception

internal class DdevPredefinedTerminalActionProviderTest : BasePlatformTestCase() {
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
    fun listOpenPredefinedTerminalActions() {
        val project = getProject()
        val ddevPredefinedTerminalActionProvider = DdevPredefinedTerminalActionProvider()

        Assertions.assertTrue(ddevPredefinedTerminalActionProvider.listOpenPredefinedTerminalActions(project).isEmpty())
    }
}
