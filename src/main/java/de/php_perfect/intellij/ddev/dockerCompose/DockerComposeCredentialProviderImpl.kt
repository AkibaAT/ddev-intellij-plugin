package de.php_perfect.intellij.ddev.dockerCompose

import com.intellij.docker.DockerCloudConfiguration
import com.intellij.docker.DockerCloudType
import com.intellij.docker.remote.DockerComposeCredentialsHolder
import com.intellij.docker.remote.DockerComposeCredentialsType
import com.intellij.docker.remote.DockerCredentialsEditor
import com.intellij.execution.configuration.EnvironmentVariablesData
import com.intellij.openapi.diagnostic.Logger
import com.intellij.remoteServer.configuration.RemoteServersManager
import java.util.Locale
import java.util.Map

class DockerComposeCredentialProviderImpl : DockerComposeCredentialProvider {
    override fun getDdevDockerComposeCredentials(dockerComposeConfig: DockerComposeConfig): DockerComposeCredentialsHolder {
        this.ensureDockerRemoteServer()

        DockerComposeCredentialProviderImpl.Companion.LOG.debug("Providing new docker credentials")

        val credentials = DockerComposeCredentialsType.getInstance().createCredentials()
        credentials.setAccountName(DockerComposeCredentialProviderImpl.Companion.DOCKER_NAME)
        credentials.setComposeFilePaths(dockerComposeConfig.composeFilePaths)
        credentials.setComposeServiceName(DockerComposeCredentialProviderImpl.Companion.SERVICE_NAME)
        credentials.setRemoteProjectPath(DockerCredentialsEditor.DEFAULT_DOCKER_PROJECT_PATH)
        credentials.setEnvs(
            EnvironmentVariablesData.create(
                Map.of<String?, String?>(
                    DockerComposeCredentialProviderImpl.Companion.COMPOSE_PROJECT_NAME_ENV,
                    "ddev-" + dockerComposeConfig.projectName.lowercase(Locale.getDefault())
                ), true
            )
        )

        return credentials
    }

    private fun ensureDockerRemoteServer() {
        val type = DockerCloudType.getInstance()
        val remoteServerManager = RemoteServersManager.getInstance()

        if (remoteServerManager.findByName<DockerCloudConfiguration?>(
                DockerComposeCredentialProviderImpl.Companion.DOCKER_NAME,
                type
            ) == null
        ) {
            remoteServerManager.addServer(
                remoteServerManager.createServer<DockerCloudConfiguration?>(
                    type,
                    DockerComposeCredentialProviderImpl.Companion.DOCKER_NAME
                )
            )
        }
    }

    companion object {
        private const val DOCKER_NAME = "Docker"
        private const val SERVICE_NAME = "web"
        private const val COMPOSE_PROJECT_NAME_ENV = "COMPOSE_PROJECT_NAME"
        private val LOG = Logger.getInstance(DockerComposeCredentialProviderImpl::class.java)
    }
}
