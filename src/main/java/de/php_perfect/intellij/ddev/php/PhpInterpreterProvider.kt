package de.php_perfect.intellij.ddev.php

import com.intellij.openapi.project.Project

interface PhpInterpreterProvider {
    fun registerInterpreter(interpreterConfig: DdevInterpreterConfig)

    companion object {
        fun getInstance(project: Project): PhpInterpreterProvider? {
            return project.getService<PhpInterpreterProvider?>(PhpInterpreterProvider::class.java)
        }
    }
}
