package de.php_perfect.intellij.ddev.cmd

import java.lang.Exception

class CommandFailedException : Exception {
    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
