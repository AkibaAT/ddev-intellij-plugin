package de.php_perfect.intellij.ddev.tutorial

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import javax.swing.JComponent

interface GotItTutorial {
    fun showStatusBarTutorial(component: JComponent, disposable: Disposable)

    fun showTerminalTutorial(component: JComponent, disposable: Disposable)

    companion object {
        fun getInstance(): GotItTutorial? {
            return ApplicationManager.getApplication().getService<GotItTutorial?>(GotItTutorial::class.java)
        }
    }
}
