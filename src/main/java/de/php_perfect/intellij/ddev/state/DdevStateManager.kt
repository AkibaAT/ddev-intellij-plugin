package de.php_perfect.intellij.ddev.state

import com.intellij.openapi.project.Project
import org.jetbrains.annotations.TestOnly

interface DdevStateManager {
    fun getState(): State

    fun initialize()

    fun reinitialize()

    fun updateConfiguration()

    fun updateDescription()

    @TestOnly
    fun resetState()

    companion object {
        @JvmStatic
        fun getInstance(project: Project): DdevStateManager {
            return project.getService<DdevStateManager>(DdevStateManager::class.java)
        }
    }
}
