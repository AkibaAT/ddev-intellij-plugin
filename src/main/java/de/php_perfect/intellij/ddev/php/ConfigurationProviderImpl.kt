package de.php_perfect.intellij.ddev.php

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.dockerCompose.DdevComposeFileLoader
import de.php_perfect.intellij.ddev.notification.DdevNotifier
import de.php_perfect.intellij.ddev.settings.DdevSettingsState

class ConfigurationProviderImpl(private val project: Project) : ConfigurationProvider {

    override fun configure(description: Description) {
        if (!DdevSettingsState.getInstance(this.project).autoConfigurePhpInterpreter) {
            return
        }

        if (description.getName() == null || description.getPhpVersion() == null) {
            return
        }

        val composeFile = DdevComposeFileLoader.getInstance(this.project)!!.load()

        if (composeFile == null || !composeFile.exists()) {
            return
        }

        val pluginManager = PluginManager.getInstance()

        for (id in REQUIRED_PLUGINS) {
            val pluginId = PluginId.findId(id)

            if (pluginId == null || pluginManager.findEnabledPlugin(pluginId) == null) {
                DdevNotifier.getInstance(this.project)!!.notifyMissingPlugin(id.toString())
                return
            }
        }

        val ddevInterpreterConfig =
            DdevInterpreterConfig(description.getName()!!, "php" + description.getPhpVersion(), composeFile.path)
        PhpInterpreterProvider.Companion.getInstance(this.project)?.registerInterpreter(ddevInterpreterConfig)
    }

    companion object {
        private val REQUIRED_PLUGINS: MutableList<String?> = mutableListOf<String?>(
            "org.jetbrains.plugins.phpstorm-remote-interpreter",
            "org.jetbrains.plugins.phpstorm-docker",
            "Docker"
        )
    }
}
