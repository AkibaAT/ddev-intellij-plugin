package de.php_perfect.intellij.ddev.cmd.parser

import java.lang.Exception

class JsonParserException : Exception {
    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable) : super(message, cause)
}
