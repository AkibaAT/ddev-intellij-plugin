package de.php_perfect.intellij.ddev.cmd.parser

import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.cmd.Versions
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

internal class JsonParserTest {
    @Test
    @Throws(JsonParserException::class)
    fun parseValidJson() {
        val expected = TestObject()
        expected.foo = "Bar"

        val json =
            "{" + "    \"level\": \"info\"," + "    \"msg\": \"Abc\"," + "    \"raw\": {" + "        \"foo\": \"Bar\"" + "    }," + "    \"time\": \"2022-02-05T14:11:53+01:00\"" + "}"

        Assertions.assertEquals(expected, JsonParserImpl().parse<Any?>(json, TestObject::class.java))
    }

    @Test
    fun parseInvalidJson() {
        val json = "{]"

        Assertions.assertThrows<JsonParserException?>(
            JsonParserException::class.java,
            Executable { JsonParserImpl().parse<Any?>(json, TestObject::class.java) })
    }

    @Test
    fun parseValidButEmptyJson() {
        val json = "{}"

        Assertions.assertThrows<JsonParserException?>(
            JsonParserException::class.java,
            Executable { JsonParserImpl().parse<Any?>(json, TestObject::class.java) })
    }

    @Test
    fun parseValidJsonWithoutRawProperty() {
        val json =
            "{" + "    \"level\": \"info\"," + "    \"msg\": \"Abc\"," + "    \"time\": \"2022-02-05T14:11:53+01:00\"" + "}"

        Assertions.assertThrows<JsonParserException?>(
            JsonParserException::class.java,
            Executable { JsonParserImpl().parse<Any?>(json, TestObject::class.java) })
    }

    @Test
    @Throws(JsonParserException::class, IOException::class)
    fun statusSuccessfully() {
        val json = Files.readString(Path.of("src/test/resources/ddev_describe.json"))
        val actual: Description = JsonParserImpl().parse<Description?>(
            json,
            de.php_perfect.intellij.ddev.cmd.Description::class.java
        )!!

        Assertions.assertEquals("8.1", actual.getPhpVersion())
    }

    @Test
    @Throws(JsonParserException::class, IOException::class)
    fun statusWithDebugSuccessfully() {
        val json = Files.readString(Path.of("src/test/resources/ddev_describe_w_debug.json"))
        val actual: Description = JsonParserImpl().parse<Description?>(
            json,
            de.php_perfect.intellij.ddev.cmd.Description::class.java
        )!!

        Assertions.assertEquals("8.1", actual.getPhpVersion())
    }


    @Test
    @Throws(JsonParserException::class, IOException::class)
    fun parseVersionSuccessfully() {
        val json = Files.readString(Path.of("src/test/resources/ddev_version.json"))
        val actual: Versions = JsonParserImpl().parse<Versions?>(
            json,
            de.php_perfect.intellij.ddev.cmd.Versions::class.java
        )!!

        Assertions.assertEquals("v1.19.0", actual.getDdevVersion())
    }
}
