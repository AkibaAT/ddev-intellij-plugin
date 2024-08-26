package de.php_perfect.intellij.ddev.php.server

import de.php_perfect.intellij.ddev.index.IndexableConfiguration
import java.net.URI
import java.util.Objects

@JvmRecord
data class ServerConfig(
    localPath: String, remotePathPath: String,
    url: URI
) : IndexableConfiguration {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as ServerConfig
        return localPath == that.localPath && remotePathPath == that.remotePathPath && url == that.url
    }

    override fun hashCode(): Int {
        return Objects.hash(localPath, remotePathPath, url)
    }

    override fun toString(): String {
        return "ServerConfig{" +
                "localPath='" + localPath + '\'' +
                ", remotePathPath='" + remotePathPath + '\'' +
                ", url=" + url +
                '}'
    }

    val localPath: String
    val remotePathPath: String
    val url: URI

    init {
        this.localPath = localPath
        this.remotePathPath = remotePathPath
        this.url = url
    }
}
