package de.php_perfect.intellij.ddev.cmd.parser

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.Scanner

class JsonParserImpl : JsonParser {
    @Throws(JsonParserException::class)
    override fun <T> parse(json: String?, typeOfT: Type?): T {
        if (json != null) {
            Scanner(json).use { scanner ->
                while (scanner.hasNextLine()) {
                    val line = scanner.nextLine()
                    val logLine = parseJson<T?>(line, typeOfT)

                    if (logLine != null && logLine.raw != null) {
                        return logLine.raw
                    }
                }
            }
        }
        throw JsonParserException("Could not parse the ddev describe output")
    }

    @Throws(JsonParserException::class)
    private fun <T> parseJson(json: String?, typeOfT: Type?): LogLine<T?>? {
        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
        val typeToken = TypeToken.getParameterized(LogLine::class.java, typeOfT).type

        try {
            return gson.fromJson<LogLine<T?>?>(json, typeToken)
        } catch (exception: JsonSyntaxException) {
            throw JsonParserException(String.format("Encountered invalid JSON '%s'", json), exception)
        }
    }
}
