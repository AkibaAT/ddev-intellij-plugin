package de.php_perfect.intellij.ddev.node

import de.php_perfect.intellij.ddev.index.IndexableConfiguration
import java.util.Objects

@JvmRecord
data class NodeInterpreterConfig(
    name: String, composeFilePath: String,
    binaryPath: String
) : IndexableConfiguration {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as NodeInterpreterConfig
        return name == that.name && composeFilePath == that.composeFilePath && binaryPath == that.binaryPath
    }

    override fun hashCode(): Int {
        return Objects.hash(name, composeFilePath, binaryPath)
    }

    override fun toString(): String {
        return "NodeInterpreterConfig{" +
                "name='" + name + '\'' +
                ", composeFilePath='" + composeFilePath + '\'' +
                ", binaryPath='" + binaryPath + '\'' +
                '}'
    }

    val name: String
    val composeFilePath: String
    val binaryPath: String

    init {
        this.name = name
        this.composeFilePath = composeFilePath
        this.binaryPath = binaryPath
    }
}
