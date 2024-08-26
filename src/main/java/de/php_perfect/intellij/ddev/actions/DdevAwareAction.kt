package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsActions
import javax.swing.Icon

abstract class DdevAwareAction : DumbAwareAction {
    constructor() : super()

    constructor(
        text: @NlsActions.ActionText String?,
        description: @NlsActions.ActionDescription String?,
        icon: Icon?
    ) : super(text, description, icon)

    override fun update(e: AnActionEvent) {
        val project = e.project

        if (project == null) {
            e.presentation.isEnabled = false
            return
        }

        e.presentation.isEnabled = this.isActive(project)
    }

    protected abstract fun isActive(project: Project): Boolean
}
