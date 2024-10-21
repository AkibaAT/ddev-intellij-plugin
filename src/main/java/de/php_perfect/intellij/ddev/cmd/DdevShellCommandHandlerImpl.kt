package de.php_perfect.intellij.ddev.cmd

import com.intellij.execution.Executor
import com.intellij.openapi.project.Project
import com.intellij.terminal.TerminalShellCommandHandler

class DdevShellCommandHandlerImpl : TerminalShellCommandHandler {
    companion object {
        const val PREFIX = "ddev "
    }

    enum class Action {
        START,
        STOP,
        RESTART,
        POWER_OFF,
        DELETE,
        SHARE,
        CONFIG
    }

    override fun execute(project: Project, workingDirectory: String?, localSession: Boolean, command: String, executor: Executor): Boolean {
        val action = parseAction(command) ?: return false
        executeAction(action, project)
        return true
    }

    override fun matches(project: Project, workingDirectory: String?, localSession: Boolean, command: String): Boolean {
        return parseAction(command) != null
    }

    private fun parseAction(command: String): Action? {
        if (!command.startsWith(PREFIX)) {
            return null
        }
        val actionString = command.substring(PREFIX.length).trim()
        return matchAction(actionString)
    }

    private fun matchAction(action: String): Action? {
        return when (action) {
            "start" -> Action.START
            "stop" -> Action.STOP
            "restart" -> Action.RESTART
            "poweroff" -> Action.POWER_OFF
            "delete" -> Action.DELETE
            "share" -> Action.SHARE
            "config" -> Action.CONFIG
            else -> null
        }
    }

    private fun executeAction(action: Action, project: Project) {
        val ddevRunner = DdevRunner.getInstance()
        when (action) {
            Action.START -> ddevRunner.start(project)
            Action.STOP -> ddevRunner.stop(project)
            Action.RESTART -> ddevRunner.restart(project)
            Action.POWER_OFF -> ddevRunner.powerOff(project)
            Action.DELETE -> ddevRunner.delete(project)
            Action.SHARE -> ddevRunner.share(project)
            Action.CONFIG -> ddevRunner.config(project)
        }
    }
}
