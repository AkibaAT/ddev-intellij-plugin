package de.php_perfect.intellij.ddev.php

import de.php_perfect.intellij.ddev.index.IndexableConfiguration
import java.util.Objects

@JvmRecord
data class DdevInterpreterConfig(
    name: String, phpVersion: String,
    composeFilePath: String
) : IndexableConfiguration {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as DdevInterpreterConfig
        return this.name == that.name && this.phpVersion == that.phpVersion && this.composeFilePath == that.composeFilePath
    }

    override fun hashCode(): Int {
        return Objects.hash(this.name, this.phpVersion, this.composeFilePath)
    }

    override fun toString(): String {
        return "DdevInterpreterConfig{" +
                "name='" + name + '\'' +
                ", phpVersion='" + phpVersion + '\'' +
                ", composeFilePath='" + composeFilePath + '\'' +
                '}'
    }

    val name: String
    val phpVersion: String
    val composeFilePath: String

    init {
        this.name = name
        this.phpVersion = phpVersion
        this.composeFilePath = composeFilePath
    }
}
