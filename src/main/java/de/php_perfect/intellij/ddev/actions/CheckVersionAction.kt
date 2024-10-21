package de.php_perfect.intellij.ddev.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import de.php_perfect.intellij.ddev.version.VersionChecker

class CheckVersionAction : AnAction(), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        VersionChecker.getInstance(project).checkDdevVersion(true)
    }
}
