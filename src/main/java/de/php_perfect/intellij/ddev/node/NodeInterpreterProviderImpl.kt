package de.php_perfect.intellij.ddev.node

import com.intellij.docker.remote.DockerComposeCredentialsHolder
import com.intellij.docker.remote.DockerComposeCredentialsType
import com.intellij.execution.ExecutionException
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterManager
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.PathMappingSettings
import com.jetbrains.nodejs.remote.NodeJSRemoteInterpreterManager
import com.jetbrains.nodejs.remote.NodeJSRemoteSdkAdditionalData
import com.jetbrains.nodejs.remote.NodeRemoteInterpreters
import de.php_perfect.intellij.ddev.dockerCompose.DockerComposeConfig
import de.php_perfect.intellij.ddev.dockerCompose.DockerComposeCredentialProvider.Companion.getInstance
import java.util.List

class NodeInterpreterProviderImpl(project: Project) : NodeInterpreterProvider {
    private val project: Project

    init {
        this.project = project
    }

    override fun configureNodeInterpreter(nodeInterpreterConfig: NodeInterpreterConfig) {
        val nodeRemoteInterpreters = NodeRemoteInterpreters.getInstance()

        if (!nodeRemoteInterpreters.getInterpreters().isEmpty()) {
            return
        }

        NodeInterpreterProviderImpl.Companion.LOG.debug("Creating nodejs interpreter")

        val credentials = getInstance()!!.getDdevDockerComposeCredentials(
            DockerComposeConfig(
                List.of<String?>(nodeInterpreterConfig.composeFilePath),
                nodeInterpreterConfig.name
            )
        )
        val sdkData = this.buildNodeJSRemoteSdkAdditionalData(credentials, nodeInterpreterConfig.binaryPath)
        nodeRemoteInterpreters.add(sdkData)

        NodeJsInterpreterManager.getInstance(this.project)
            .setInterpreterRef(NodeJsInterpreterRef.create(sdkData.getSdkId()))
    }

    private fun buildNodeJSRemoteSdkAdditionalData(
        credentials: DockerComposeCredentialsHolder?,
        binaryPath: String
    ): NodeJSRemoteSdkAdditionalData {
        val sdkData = NodeJSRemoteSdkAdditionalData(binaryPath)
        sdkData.setCredentials<DockerComposeCredentialsHolder?>(
            DockerComposeCredentialsType.getInstance().getCredentialsKey(), credentials
        )
        sdkData.setHelpersPath(NODEJS_HELPERS_PATH)
        sdkData.setPathMappings(this.loadPathMappings(sdkData))

        return sdkData
    }

    private fun loadPathMappings(sdkData: NodeJSRemoteSdkAdditionalData): PathMappingSettings? {
        val nodeRemoteInterpreterManager = NodeJSRemoteInterpreterManager.getInstance()

        try {
            return nodeRemoteInterpreterManager.setupMappings(this.project, sdkData)
        } catch (e: ExecutionException) {
            return null
        }
    }

    companion object {
        const val NODEJS_HELPERS_PATH: String = ".webstorm_nodejs_helpers"
        private val LOG = Logger.getInstance(NodeInterpreterProviderImpl::class.java)
    }
}
