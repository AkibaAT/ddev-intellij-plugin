package de.php_perfect.intellij.ddev.database

import de.php_perfect.intellij.ddev.index.IndexableConfiguration
import java.util.Objects

@JvmRecord
data class DataSourceConfig(
    name: String, description: String, type: Type,
    version: String, host: String, port: Int, database: String,
    username: String, password: String
) : IndexableConfiguration {
    enum class Type {
        MYSQL,
        MARIADB,
        POSTGRESQL,
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as DataSourceConfig
        return port == that.port && name == that.name && description == that.description && type == that.type && version == that.version && host == that.host && database == that.database && username == that.username && password == that.password
    }

    override fun hashCode(): Int {
        return Objects.hash(name, description, type, version, host, port, database, username, password)
    }

    override fun toString(): String {
        return "DataSourceConfig{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", version='" + version + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", database='" + database + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}'
    }

    val name: String
    val description: String
    val type: Type
    val version: String
    val host: String
    val port: Int
    val database: String
    val username: String
    val password: String

    init {
        this.name = name
        this.description = description
        this.type = type
        this.version = version
        this.host = host
        this.port = port
        this.database = database
        this.username = username
        this.password = password
    }
}
