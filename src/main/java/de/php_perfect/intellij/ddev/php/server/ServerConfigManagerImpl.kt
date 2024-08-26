package de.php_perfect.intellij.ddev.php.server

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.PathMappingSettings
import com.jetbrains.php.config.servers.PhpServer
import com.jetbrains.php.config.servers.PhpServersWorkspaceStateComponent
import de.php_perfect.intellij.ddev.index.ManagedConfigurationIndex

class ServerConfigManagerImpl(project: Project) : ServerConfigManager {
    private val project: Project

    init {
        this.project = project
    }

    override fun configure(serverConfig: ServerConfig) {
        val hash = serverConfig.hashCode()
        val fqdn = serverConfig.url.getHost()
        val managedConfigurationIndex = ManagedConfigurationIndex.getInstance(this.project)
        val indexEntry = managedConfigurationIndex!!.get(ServerConfig::class.java)
        val servers = PhpServersWorkspaceStateComponent.getInstance(project).getServers()
        var phpServer: PhpServer? = null

        if (indexEntry != null && (servers.stream()
                .filter { currentPhpServer: PhpServer? -> currentPhpServer!!.getId() == indexEntry.id }
                .findFirst()
                .orElse(null).also { phpServer = it }) != null && indexEntry.hashEquals(hash)
        ) {
            ServerConfigManagerImpl.Companion.LOG.debug(String.format("server configuration %s is up to date", fqdn))
            return
        }

        ServerConfigManagerImpl.Companion.LOG.debug(String.format("Updating server configuration %s", fqdn))

        if (phpServer == null) {
            phpServer = servers.stream()
                .filter { currentPhpServer: PhpServer? -> currentPhpServer!!.getName() == ServerConfigManagerImpl.Companion.LEGACY_SERVER_NAME }
                .findFirst()
                .orElse(null)
        }

        if (phpServer == null) {
            phpServer = PhpServer()
            servers.add(phpServer)
        }

        phpServer.setName(fqdn)
        phpServer.setHost(fqdn)
        phpServer.setPort(80)
        phpServer.setUsePathMappings(true)

        val mappings = phpServer.getMappings()
        val mapping = PathMappingSettings.PathMapping(serverConfig.localPath, serverConfig.remotePathPath)
        mappings.clear()
        mappings.add(mapping)

        managedConfigurationIndex.set(phpServer.getId(), ServerConfig::class.java, hash)
    }

    companion object {
        private const val LEGACY_SERVER_NAME = "DDEV"
        private val LOG = Logger.getInstance(ServerConfigManagerImpl::class.java)
    }
}
