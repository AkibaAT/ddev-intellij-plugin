package de.php_perfect.intellij.ddev.cmd

import com.google.gson.annotations.SerializedName
import java.util.Objects

class Versions(ddevVersion: String?, dockerVersion: String?, dockerComposeVersion: String?, dockerPlatform: String?) {
    @SerializedName("DDEV version")
    private val ddevVersion: String?

    @SerializedName("docker")
    private val dockerVersion: String?

    @SerializedName("docker-compose")
    private val dockerComposeVersion: String?

    @SerializedName("docker-platform")
    private val dockerPlatform: String?

    init {
        this.ddevVersion = ddevVersion
        this.dockerVersion = dockerVersion
        this.dockerComposeVersion = dockerComposeVersion
        this.dockerPlatform = dockerPlatform
    }

    fun getDdevVersion(): String? {
        return ddevVersion
    }

    fun getDockerVersion(): String? {
        return dockerVersion
    }

    fun getDockerComposeVersion(): String? {
        return dockerComposeVersion
    }

    fun getDockerPlatform(): String? {
        return dockerPlatform
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val versions = o as Versions
        return getDdevVersion() == versions.getDdevVersion() && getDockerVersion() == versions.getDockerVersion() && getDockerComposeVersion() == versions.getDockerComposeVersion() && getDockerPlatform() == versions.getDockerPlatform()
    }

    override fun hashCode(): Int {
        return Objects.hash(getDdevVersion(), getDockerVersion(), getDockerComposeVersion(), getDockerPlatform())
    }

    override fun toString(): String {
        return "Versions{" +
                "ddevVersion='" + ddevVersion + '\'' +
                ", dockerVersion='" + dockerVersion + '\'' +
                ", dockerComposeVersion='" + dockerComposeVersion + '\'' +
                ", dockerPlatform='" + dockerPlatform + '\'' +
                '}'
    }
}
