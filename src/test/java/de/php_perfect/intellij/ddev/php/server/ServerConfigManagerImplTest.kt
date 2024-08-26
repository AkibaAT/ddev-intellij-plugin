package de.php_perfect.intellij.ddev.php.server

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.php.config.servers.PhpServersWorkspaceStateComponent
import de.php_perfect.intellij.ddev.php.server.ServerConfigManager.Companion.getInstance
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Exception
import java.net.URI
import java.net.URISyntaxException
import java.util.Objects

internal class ServerConfigManagerImplTest : BasePlatformTestCase() {
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
    @Throws(URISyntaxException::class)
    fun configure() {
        val serverConfig = ServerConfig(
            Objects.requireNonNull<String?>(this.getProject().getBasePath()),
            "/var/www/html",
            URI("https://test.ddev.site")
        )

        val serverConfigManager = getInstance(this.getProject())
        serverConfigManager!!.configure(serverConfig)
        // Check server gets replaced
        serverConfigManager.configure(serverConfig)

        this.assertServerConfigMatches(serverConfig)
    }

    private fun assertServerConfigMatches(serverConfig: ServerConfig) {
        val servers = PhpServersWorkspaceStateComponent.getInstance(this.getProject()).getServers()

        Assert.assertEquals(1, servers.size.toLong())

        val server = servers.get(0)
        Assert.assertEquals("test.ddev.site", server.getName())
        Assert.assertEquals("test.ddev.site", server.getHost())

        val mappings = server.getMappings()

        Assert.assertEquals(1, mappings.size.toLong())

        val mapping = mappings.get(0)

        Assert.assertEquals(serverConfig.localPath, mapping.getLocalRoot())
        Assert.assertEquals(serverConfig.remotePathPath, mapping.getRemoteRoot())
    }
}
