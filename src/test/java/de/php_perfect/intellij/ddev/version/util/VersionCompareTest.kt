package de.php_perfect.intellij.ddev.version.util

import de.php_perfect.intellij.ddev.version.Version
import de.php_perfect.intellij.ddev.version.util.VersionCompare.needsUpdate
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class VersionCompareTest {
    @Test
    fun needsUpdateMajor() {
        Assertions.assertTrue(needsUpdate(Version("1.0.0"), Version("2.0.0")))
    }

    @Test
    fun needsUpdateMinor() {
        Assertions.assertTrue(needsUpdate(Version("1.0.0"), Version("1.1.0")))
    }

    @Test
    fun needsUpdatePatch() {
        Assertions.assertTrue(needsUpdate(Version("1.0.0"), Version("1.0.1")))
    }

    @Test
    fun needsNoUpdateSame() {
        Assertions.assertFalse(needsUpdate(Version("1.0.0"), Version("1.0.0")))
    }

    @Test
    fun needsNoUpdateOnRc() {
        Assertions.assertFalse(needsUpdate(Version("v1.19.0-rc1"), Version("1.18.9")))
    }

    @Test
    fun needsNoUpdateLowerMinor() {
        Assertions.assertFalse(needsUpdate(Version("1.0.0"), Version("0.9.0")))
    }
}
