package de.php_perfect.intellij.ddev.terminal

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import de.php_perfect.intellij.ddev.state.DdevConfigLoader
import de.php_perfect.intellij.ddev.state.DdevStateManager
import de.php_perfect.intellij.ddev.state.MockDdevConfigLoader
import org.jetbrains.plugins.terminal.ShellStartupOptions.Builder.build
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.lang.Exception
import java.util.Map
import java.util.concurrent.ExecutionException

internal class DdevTerminalRunnerTest : BasePlatformTestCase() {
    @BeforeEach
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun createProcessNotExistentDdev() {
        val project = getProject()
        val ddevTerminalRunner = DdevTerminalRunner(project)

        val state = DdevStateManager.getInstance(project)!!.getState()

        val field = state.javaClass.getDeclaredField("ddevBinary")
        field.setAccessible(true)
        field.set(state, null)

        val envVariables = Map.of<String?, String?>()
        val builder: Builder = Builder(project.getBasePath(), null, null, null, null, null, envVariables)

        Assertions.assertThrowsExactly<ExecutionException?>(
            ExecutionException::class.java,
            Executable { ddevTerminalRunner.createProcess(builder.build()) })
    }

    @Test
    fun terminalIsNotPersistent() {
        val project = getProject()
        val ddevTerminalRunner = DdevTerminalRunner(project)

        Assertions.assertFalse(ddevTerminalRunner.isTerminalSessionPersistent())
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
