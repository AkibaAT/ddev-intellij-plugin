package de.php_perfect.intellij.ddev.dockerCompose

import de.php_perfect.intellij.ddev.index.IndexableConfiguration
import java.util.Objects

@JvmRecord
data class DockerComposeConfig(
    composeFilePaths: MutableList<String?>,
    projectName: String
) : IndexableConfiguration {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as DockerComposeConfig
        return composeFilePaths == that.composeFilePaths && projectName == that.projectName
    }

    override fun hashCode(): Int {
        return Objects.hash(composeFilePaths, projectName)
    }

    override fun toString(): String {
        return "DockerComposeConfig{" +
                "composeFilePaths=" + composeFilePaths +
                ", projectName='" + projectName + '\'' +
                '}'
    }

    val composeFilePaths: MutableList<String?>
    val projectName: String

    init {
        this.composeFilePaths = composeFilePaths
        this.projectName = projectName
    }
}
