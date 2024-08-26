package de.php_perfect.intellij.ddev

import com.intellij.openapi.project.Project

interface DdevConfigArgumentProvider {
    fun getAdditionalArguments(project: Project): MutableList<String>
}
