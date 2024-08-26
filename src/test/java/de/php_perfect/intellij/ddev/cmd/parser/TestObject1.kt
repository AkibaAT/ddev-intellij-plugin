package de.php_perfect.intellij.ddev.cmd.parser

import java.util.Objects

internal class TestObject {
    var foo: String? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as TestObject
        return foo == that.foo
    }

    override fun hashCode(): Int {
        return Objects.hash(foo)
    }
}
