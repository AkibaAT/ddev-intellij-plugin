package de.php_perfect.intellij.ddev.cmd.wsl

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.wsl.WSLCommandLineOptions
import com.intellij.execution.wsl.WSLDistribution

object WslAware {
    fun <T : GeneralCommandLine?> patchCommandLine(commandLine: T?): T? {
        return patchCommandLine<T?>(commandLine, false)
    }

    fun <T : GeneralCommandLine?> patchCommandLine(commandLine: T?, loginShell: Boolean): T? {
        val distribution: WSLDistribution? =
            getDistributionByWindowsUncPath.getDistributionByWindowsUncPath(commandLine!!.getWorkDirectory().getPath())

        if (distribution == null) {
            return commandLine
        }

        try {
            return applyWslPatch<T?>(commandLine, distribution, loginShell)
        } catch (ignored: ExecutionException) {
            return commandLine
        }
    }

    @Throws(ExecutionException::class)
    private fun <T : GeneralCommandLine?> applyWslPatch(
        generalCommandLine: T?,
        distribution: WSLDistribution,
        loginShell: Boolean
    ): T {
        val options = WSLCommandLineOptions()
            .setExecuteCommandInLoginShell(loginShell)

        return distribution.patchCommandLine<T?>(generalCommandLine, null, options)
    }
}
