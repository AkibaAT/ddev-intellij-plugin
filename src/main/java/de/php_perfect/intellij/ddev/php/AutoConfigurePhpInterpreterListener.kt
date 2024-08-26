package de.php_perfect.intellij.ddev.php

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.DescriptionChangedListener
import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.dockerCompose.DdevComposeFileLoader.Companion.getInstance
import de.php_perfect.intellij.ddev.dockerCompose.DockerComposeCredentialProvider.Companion.getInstance
import de.php_perfect.intellij.ddev.index.ManagedConfigurationIndex.Companion.getInstance
import de.php_perfect.intellij.ddev.notification.DdevNotifier.Companion.getInstance

class AutoConfigurePhpInterpreterListener(project: Project) : DescriptionChangedListener {
    private val project: Project

    init {
        this.project = project
    }

    override fun onDescriptionChanged(description: Description?) {
        if (description == null) {
            return
        }

        ConfigurationProvider.Companion.getInstance(this.project).configure(description)
    }
}
