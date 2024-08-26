package de.php_perfect.intellij.ddev.state

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Exception

internal class StateWatcherTest : BasePlatformTestCase() {
    private var stateWatcher: StateWatcherImpl? = null

    @BeforeEach
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        this.stateWatcher = StateWatcherImpl(this.getProject())
    }

    @Test
    fun startWatching() {
        stateWatcher!!.startWatching()
        Assert.assertTrue(stateWatcher!!.isWatching())
    }

    @Test
    fun startWatchingTwice() {
        stateWatcher!!.startWatching()
        stateWatcher!!.startWatching()
        Assert.assertTrue(stateWatcher!!.isWatching())
    }

    @Test
    fun startStopWatching() {
        stateWatcher!!.startWatching()
        stateWatcher!!.stopWatching()
        Assert.assertFalse(stateWatcher!!.isWatching())
    }

    @Test
    fun startStopWatchingWithoutStart() {
        stateWatcher!!.stopWatching()
        Assert.assertFalse(stateWatcher!!.isWatching())
    }

    @AfterEach
    @Throws(Exception::class)
    override fun tearDown() {
        this.stateWatcher!!.dispose()
        super.tearDown()
    }
}
