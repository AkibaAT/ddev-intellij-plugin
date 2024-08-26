package de.php_perfect.intellij.ddev.serviceActions

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.DescriptionChangedListener
import de.php_perfect.intellij.ddev.cmd.Description

class ServiceActionChangedListener(private val project: Project) : DescriptionChangedListener {

    override fun onDescriptionChanged(description: Description?) {
        ServiceActionManager.Companion.getInstance(this.project)?.updateActionsByDescription(description)
    }
}
