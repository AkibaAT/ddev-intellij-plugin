package de.php_perfect.intellij.ddev.php

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.cmd.Description

interface ConfigurationProvider {
    fun configure(description: Description)

    companion object {
        fun getInstance(project: Project): ConfigurationProvider? {
            return project.getService<ConfigurationProvider?>(ConfigurationProvider::class.java)
        }
    }
}
