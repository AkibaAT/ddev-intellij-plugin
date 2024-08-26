package de.php_perfect.intellij.ddev.serviceActions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.cmd.Description

interface ServiceActionManager {
    fun getServiceActions(): Array<AnAction?>

    fun updateActionsByDescription(description: Description?)

    companion object {
        fun getInstance(project: Project): ServiceActionManager? {
            return project.getService<ServiceActionManager?>(ServiceActionManager::class.java)
        }
    }
}
