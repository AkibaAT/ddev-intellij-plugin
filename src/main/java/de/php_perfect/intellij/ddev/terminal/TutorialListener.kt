package de.php_perfect.intellij.ddev.terminal

import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.openapi.wm.impl.InternalDecorator
import com.intellij.ui.ComponentUtil
import de.php_perfect.intellij.ddev.tutorial.GotItTutorial
import org.jetbrains.plugins.terminal.TerminalToolWindowFactory
import org.jetbrains.plugins.terminal.action.TerminalNewPredefinedSessionAction
import java.util.function.Consumer

class TutorialListener : ToolWindowManagerListener {
    override fun toolWindowShown(toolWindow: ToolWindow) {
        if (TerminalToolWindowFactory.TOOL_WINDOW_ID != toolWindow.id) {
            return
        }

        val decorator =
            ComponentUtil.getParentOfType<InternalDecorator?>(InternalDecorator::class.java, toolWindow.component)

        ComponentUtil.findComponentsOfType<ActionButton?>(decorator, ActionButton::class.java).stream()
            .filter { button: ActionButton? -> button!!.action is TerminalNewPredefinedSessionAction }
            .findFirst().ifPresent(Consumer { button: ActionButton? ->
                GotItTutorial.getInstance()?.showTerminalTutorial(button!!, toolWindow.disposable)
            })
    }
}
