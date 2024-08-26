package de.php_perfect.intellij.ddev.php.server

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.DescriptionChangedListener
import de.php_perfect.intellij.ddev.cmd.Description
import java.net.URI
import java.net.URISyntaxException

class ConfigureServerListener(private val project: Project) : DescriptionChangedListener {

    override fun onDescriptionChanged(description: Description?) {
        val localPath = this.project.basePath

        if (description == null || localPath == null) {
            return
        }

        if (description.getPrimaryUrl() == null) {
            return
        }


        var uri: URI?
        try {
            uri = URI(description.getPrimaryUrl())
        } catch (ignored: URISyntaxException) {
            return
        }

        val serverConfig = ServerConfig(localPath, "/var/www/html", uri)
        ServerConfigManager.Companion.getInstance(this.project)?.configure(serverConfig)
    }
}
