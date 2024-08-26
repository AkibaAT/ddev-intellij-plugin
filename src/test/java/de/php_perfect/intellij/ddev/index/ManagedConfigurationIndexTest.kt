package de.php_perfect.intellij.ddev.index

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import de.php_perfect.intellij.ddev.index.ManagedConfigurationIndex.Companion.getInstance
import de.php_perfect.intellij.ddev.index.ManagedConfigurationIndexTest.SomeConfiguration
import de.php_perfect.intellij.ddev.index.ManagedConfigurationIndexTest.SomeOtherConfiguration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Exception

internal class ManagedConfigurationIndexTest : BasePlatformTestCase() {
    private class SomeConfiguration : IndexableConfiguration {
        override fun hashCode(): Int {
            return 1
        }
    }

    private class SomeOtherConfiguration : IndexableConfiguration {
        override fun hashCode(): Int {
            return 2
        }
    }

    @BeforeEach
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun addToIndexCheckId() {
        val managedConfigurationIndex = getInstance(this.getProject())
        managedConfigurationIndex!!.set("as73asvb324", SomeConfiguration::class.java, 123)

        assertTrue(managedConfigurationIndex.isManaged("as73asvb324", SomeConfiguration::class.java))
        assertFalse(managedConfigurationIndex.isManaged("fooBar", SomeConfiguration::class.java))
    }

    @Test
    fun getFromIndex() {
        val managedConfigurationIndex = getInstance(this.getProject())
        managedConfigurationIndex!!.set("as73asvb324", SomeConfiguration::class.java, 123)

        assertSame("as73asvb324", managedConfigurationIndex.get(SomeConfiguration::class.java)!!.id)
        assertSame(null, managedConfigurationIndex.get(SomeOtherConfiguration::class.java))
    }

    @Test
    fun addToIndexCheckType() {
        val key = "as73asvb324"
        val managedConfigurationIndex = getInstance(this.getProject())
        managedConfigurationIndex!!.set(key, SomeConfiguration::class.java, 123)

        assertTrue(managedConfigurationIndex.isManaged(key, SomeConfiguration::class.java))
        assertFalse(managedConfigurationIndex.isManaged(key, SomeOtherConfiguration::class.java))
    }

    @Test
    fun removeIndex() {
        val key = "as73asvb324"
        val managedConfigurationIndex = getInstance(this.getProject())

        managedConfigurationIndex!!.set(key, SomeConfiguration::class.java, 123)
        assertTrue(managedConfigurationIndex.isManaged(key, SomeConfiguration::class.java))

        managedConfigurationIndex.remove(SomeConfiguration::class.java)
        assertFalse(managedConfigurationIndex.isManaged(key, SomeConfiguration::class.java))
    }

    @Test
    fun purgeIndex() {
        val key = "as73asvb324"
        val managedConfigurationIndex = getInstance(this.getProject())

        managedConfigurationIndex!!.set(key, SomeConfiguration::class.java, 123)
        assertTrue(managedConfigurationIndex.isManaged(key, SomeConfiguration::class.java))

        managedConfigurationIndex.purge()
        assertFalse(managedConfigurationIndex.isManaged(key, SomeConfiguration::class.java))
    }

    @Test
    fun isUpToDate() {
        val key = "as73asvb324"
        val managedConfigurationIndex = getInstance(this.getProject())

        managedConfigurationIndex!!.set(key, SomeConfiguration::class.java, 123)
        assertTrue(managedConfigurationIndex.isUpToDate(SomeConfiguration::class.java, 123))
        assertFalse(managedConfigurationIndex.isUpToDate(SomeConfiguration::class.java, 321))
    }

    @Test
    fun isUpToDateNonExistent() {
        val managedConfigurationIndex = getInstance(this.getProject())

        assertFalse(managedConfigurationIndex!!.isUpToDate(SomeConfiguration::class.java, 123))
    }

    @AfterEach
    @Throws(Exception::class)
    override fun tearDown() {
        getInstance(this.getProject())!!.purge()

        super.tearDown()
    }
}
