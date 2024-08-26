package de.php_perfect.intellij.ddev.cmd

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Exception

class DdevShellCommandHandlerTest : BasePlatformTestCase() {
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
    fun nonDdevCommand() {
        val ddevShellCommandHandlerImpl = DdevShellCommandHandlerImpl()

        Assertions.assertFalse(ddevShellCommandHandlerImpl.matches(this.getProject(), null, true, "cat abc"))
    }

    @Test
    fun incompleteDdevCommand() {
        val ddevShellCommandHandlerImpl = DdevShellCommandHandlerImpl()

        Assertions.assertFalse(ddevShellCommandHandlerImpl.matches(this.getProject(), null, true, "ddev "))
    }

    @Test
    fun unkownDdevCommand() {
        val ddevShellCommandHandlerImpl = DdevShellCommandHandlerImpl()

        Assertions.assertFalse(ddevShellCommandHandlerImpl.matches(this.getProject(), null, true, "ddev foo"))
    }

    @Test
    fun ddevCommand() {
        val ddevShellCommandHandlerImpl = DdevShellCommandHandlerImpl()

        Assertions.assertTrue(ddevShellCommandHandlerImpl.matches(this.getProject(), null, true, "ddev start"))
    }
}
