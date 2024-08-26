package de.php_perfect.intellij.ddev.state

import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.nio.file.Files
import java.util.ArrayList

internal class DdevConfigLoaderTest : BasePlatformTestCase() {
    private val files: MutableList<File> = ArrayList<File>()

    @BeforeEach
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun nonExistentConfig() {
        Assertions.assertFalse(DdevConfigLoaderImpl(this.getProject()).exists())
    }

    @Test
    fun existentConfig() {
        val project = this.getProject()

        val ddevConfig = File(project.getBasePath() + "/.ddev/config.yaml")
        this.files.add(ddevConfig)
        ddevConfig.deleteOnExit()

        try {
            FileUtil.writeToFile(ddevConfig, "name: test", true)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        Assertions.assertTrue(DdevConfigLoaderImpl(project).exists())
    }

    @Test
    fun loadNonExisting() {
        Assertions.assertNull(DdevConfigLoaderImpl(this.getProject()).load())
    }

    @Test
    fun loadExisting() {
        val project = this.getProject()

        val ddevConfig = File(project.getBasePath() + "/.ddev/config.yaml")
        this.files.add(ddevConfig)
        ddevConfig.deleteOnExit()

        try {
            FileUtil.writeToFile(ddevConfig, "name: test", true)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        Assertions.assertInstanceOf<VirtualFile?>(VirtualFile::class.java, DdevConfigLoaderImpl(project).load())
    }

    @AfterEach
    @Throws(Exception::class)
    override fun tearDown() {
        for (file in this.files) {
            Files.deleteIfExists(file.toPath())
        }

        super.tearDown()
    }
}
