package de.php_perfect.intellij.ddev

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import de.php_perfect.intellij.ddev.errorReporting.SentrySdkInitializer.init
import de.php_perfect.intellij.ddev.state.DdevStateManager.Companion.getInstance

class InitPluginActivity : ProjectActivity, DumbAware {
    override suspend fun execute(project: Project) {
        init()
        ApplicationManager.getApplication().executeOnPooledThread(Runnable { getInstance(project)!!.initialize() })
    }
}
