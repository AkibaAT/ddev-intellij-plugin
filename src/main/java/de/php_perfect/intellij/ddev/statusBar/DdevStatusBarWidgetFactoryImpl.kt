package de.php_perfect.intellij.ddev.statusBar

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.impl.status.widget.StatusBarEditorBasedWidgetFactory
import de.php_perfect.intellij.ddev.DdevIntegrationBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls

class DdevStatusBarWidgetFactoryImpl : StatusBarEditorBasedWidgetFactory() {
    override fun getId(): @NonNls String {
        return DdevStatusBarWidgetImpl.Companion.WIDGET_ID
    }

    override fun getDisplayName(): @Nls String {
        return DdevIntegrationBundle.message("statusBar.displayName")
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean {
        val project = statusBar.project
        return project != null
    }

    override fun createWidget(project: Project): StatusBarWidget {
        return DdevStatusBarWidgetImpl(project)
    }
}
