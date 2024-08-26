package de.php_perfect.intellij.ddev.php

import com.intellij.openapi.project.Project
import com.jetbrains.php.config.PhpLanguageLevel
import de.php_perfect.intellij.ddev.DdevConfigArgumentProvider
import java.util.List

class PhpVersionArgumentProvider : DdevConfigArgumentProvider {
    override fun getAdditionalArguments(project: Project): MutableList<String> {
        return List.of<String>("--php-version", PhpLanguageLevel.current(project).getPresentableName())
    }
}
