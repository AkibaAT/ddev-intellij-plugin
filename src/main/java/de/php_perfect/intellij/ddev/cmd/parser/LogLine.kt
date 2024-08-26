package de.php_perfect.intellij.ddev.cmd.parser

@JvmRecord
data class LogLine<T>(raw: T?) {
    val raw: T?

    init {
        this.raw = raw
    }
}
