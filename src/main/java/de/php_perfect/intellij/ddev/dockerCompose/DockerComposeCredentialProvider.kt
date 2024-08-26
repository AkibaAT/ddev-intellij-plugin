package de.php_perfect.intellij.ddev.dockerCompose

import com.intellij.docker.remote.DockerComposeCredentialsHolder
import com.intellij.openapi.application.ApplicationManager

interface DockerComposeCredentialProvider {
    fun getDdevDockerComposeCredentials(dockerComposeConfig: DockerComposeConfig): DockerComposeCredentialsHolder?

    companion object {
        @JvmStatic
        fun getInstance(): DockerComposeCredentialProvider? {
            return ApplicationManager.getApplication()
                .getService<DockerComposeCredentialProvider?>(DockerComposeCredentialProvider::class.java)
        }
    }
}
