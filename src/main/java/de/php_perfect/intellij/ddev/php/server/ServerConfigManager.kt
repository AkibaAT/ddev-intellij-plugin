package de.php_perfect.intellij.ddev.php.server

import com.intellij.openapi.project.Project

interface ServerConfigManager {
    fun configure(serverConfig: ServerConfig)

    companion object {
        @JvmStatic
        fun getInstance(project: Project): ServerConfigManager? {
            return project.getService<ServerConfigManager?>(ServerConfigManager::class.java)
        }
    }
}
