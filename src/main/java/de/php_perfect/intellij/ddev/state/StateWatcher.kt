package de.php_perfect.intellij.ddev.state

import com.intellij.openapi.project.Project

interface StateWatcher {
    fun startWatching()

    fun stopWatching()

    fun isWatching(): Boolean

    companion object {
        fun getInstance(project: Project): StateWatcher? {
            return project.getService<StateWatcher?>(StateWatcher::class.java)
        }
    }
}
