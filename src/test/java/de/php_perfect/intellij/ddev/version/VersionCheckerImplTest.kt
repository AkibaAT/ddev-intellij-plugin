package de.php_perfect.intellij.ddev.version

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Exception

internal class VersionCheckerImplTest : BasePlatformTestCase() {
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
    fun checkDdevVersion() {
        val versionChecker = VersionCheckerImpl(this.getProject())
        versionChecker.checkDdevVersion()
    }

    @Test
    fun checkDdevVersionWithConfirmation() {
        val versionChecker = VersionCheckerImpl(this.getProject())
        versionChecker.checkDdevVersion(true)
    }
}
