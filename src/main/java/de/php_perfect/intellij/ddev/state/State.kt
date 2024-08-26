package de.php_perfect.intellij.ddev.state

import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.version.Version

interface State {
    fun isBinaryConfigured(): Boolean

    fun isAvailable(): Boolean

    fun isConfigured(): Boolean

    fun getDdevVersion(): Version?

    fun getDescription(): Description?

    fun getDdevBinary(): String?
}
