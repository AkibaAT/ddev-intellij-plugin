package de.php_perfect.intellij.ddev.cmd.parser

import com.intellij.openapi.application.ApplicationManager
import java.lang.reflect.Type

interface JsonParser {
    @Throws(JsonParserException::class)
    fun <T> parse(json: String?, typeOfT: Type?): T

    companion object {
        @JvmStatic
        fun getInstance(): JsonParser {
            return ApplicationManager.getApplication().getService<JsonParser>(JsonParser::class.java)
        }
    }
}
