package de.php_perfect.intellij.ddev.version

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class VersionTest {
    @ParameterizedTest
    @ValueSource(strings = ["1.26.6", "v1.26.6", "1.26.6-DEBUG"])
    fun versionsWithPrefixAndSuffixAreParsedCorrectly(versionString: String) {
        val version = Version(versionString)

        Assertions.assertArrayEquals(intArrayOf(1, 26, 6), version.numbers)
    }

    @Test
    fun compareTo_withEarlierVersion_isGreaterThan() {
        Assertions.assertEquals(1, Version("2.0.0").compareTo(Version("1.0.0")))
    }

    @Test
    fun compareTo_withSameVersion_isEqual() {
        Assertions.assertEquals(0, Version("2.0.0").compareTo(Version("2.0.0")))
    }

    @Test
    fun compareTo_withLaterVersion_isLessThan() {
        Assertions.assertEquals(-1, Version("1.0.0").compareTo(Version("2.0.0")))
    }

    @Test
    fun compareTo_withMorePreciseSameVersion_isFalse() {
        Assertions.assertEquals(0, Version("1").compareTo(Version("1.0.0")))
    }

    @Test
    fun compareTo_withMorePreciseEarlierVersion_isFalse() {
        Assertions.assertEquals(1, Version("2").compareTo(Version("1.0.0")))
    }

    @Test
    fun compareTo_withMorePreciseLaterVersion_isLessThan() {
        Assertions.assertEquals(-1, Version("1").compareTo(Version("1.0.1")))
    }
}
