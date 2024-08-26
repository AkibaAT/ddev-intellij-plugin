package de.php_perfect.intellij.ddev.php

import com.intellij.docker.remote.DockerComposeCredentialsHolder
import com.intellij.docker.remote.DockerComposeCredentialsType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.remote.RemoteMappingsManager
import com.intellij.util.PathMappingSettings
import com.jetbrains.php.composer.ComposerDataService
import com.jetbrains.php.config.PhpProjectConfigurationFacade
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import com.jetbrains.php.config.interpreters.PhpInterpretersPhpInfoCacheImpl
import com.jetbrains.php.config.phpInfo.PhpInfoUtil
import com.jetbrains.php.remote.composer.ComposerRemoteInterpreterExecution
import com.jetbrains.php.remote.docker.compose.PhpDockerComposeStartCommand
import com.jetbrains.php.remote.docker.compose.PhpDockerComposeTypeData
import com.jetbrains.php.remote.interpreter.PhpRemoteSdkAdditionalData
import com.jetbrains.php.run.remote.PhpRemoteInterpreterManager
import de.php_perfect.intellij.ddev.dockerCompose.DockerComposeConfig
import de.php_perfect.intellij.ddev.dockerCompose.DockerComposeCredentialProvider.Companion.getInstance
import de.php_perfect.intellij.ddev.index.ManagedConfigurationIndex
import de.php_perfect.intellij.ddev.notification.DdevNotifier
import java.util.List

class PhpInterpreterProviderImpl(project: Project) : PhpInterpreterProvider {
    private val project: Project

    init {
        this.project = project
    }

    override fun registerInterpreter(interpreterConfig: DdevInterpreterConfig) {
        val hash = interpreterConfig.hashCode()
        val name = interpreterConfig.name
        val interpretersManager = PhpInterpretersManagerImpl.getInstance(this.project)
        val managedConfigurationIndex = ManagedConfigurationIndex.getInstance(this.project)
        val indexEntry = managedConfigurationIndex!!.get(DdevInterpreterConfig::class.java)

        var interpreter: PhpInterpreter? = null
        if (indexEntry != null && (interpretersManager.findInterpreterById(indexEntry.id)
                .also { interpreter = it }) != null && indexEntry.hashEquals(hash)
        ) {
            PhpInterpreterProviderImpl.Companion.LOG.debug(
                String.format(
                    "PHP interpreter configuration %s is up to date",
                    name
                )
            )
            return
        }

        PhpInterpreterProviderImpl.Companion.LOG.debug(String.format("Updating PHP interpreter configuration %s", name))

        if (interpreter == null) {
            interpreter =
                interpretersManager.findInterpreter(PhpInterpreterProviderImpl.Companion.LEGACY_INTERPRETER_NAME)
        }

        if (interpreter == null) {
            interpreter = PhpInterpreter()
            interpreter.setIsProjectLevel(true)
            val interpreters = interpretersManager.getInterpreters()
            interpreters.add(interpreter)
            interpretersManager.setInterpreters(interpreters)
        }

        this.updateInterpreter(interpreter, interpreterConfig)
        this.loadPhpInfo(interpreter)
        this.setDefaultIfNotSet(interpreter)
        this.updateComposerInterpreterIfNotSet(interpreter)
        this.updateRemoteMapping(interpreter)

        managedConfigurationIndex.set(interpreter.getId(), DdevInterpreterConfig::class.java, hash)
        DdevNotifier.getInstance(project)!!.notifyPhpInterpreterUpdated(interpreterConfig.phpVersion)
    }

    private fun updateRemoteMapping(interpreter: PhpInterpreter) {
        val pathMapping = PathMappingSettings.PathMapping(project.getBasePath(), "/var/www/html")
        val mappings = RemoteMappingsManager.Mappings()
        mappings.setServerId("php", interpreter.getId())
        mappings.setSettings(List.of<PathMappingSettings.PathMapping?>(pathMapping))

        RemoteMappingsManager.getInstance(project).setForServer(mappings)
    }

    private fun updateComposerInterpreterIfNotSet(interpreter: PhpInterpreter) {
        val composerSettings = ComposerDataService.getInstance(project)

        if (composerSettings.getComposerExecution().getInterpreterId() != interpreter.getId()) {
            composerSettings.setComposerExecution(ComposerRemoteInterpreterExecution(interpreter.getName(), "composer"))
        }
    }

    private fun updateInterpreter(interpreter: PhpInterpreter, interpreterConfig: DdevInterpreterConfig) {
        val credentials = getInstance()!!.getDdevDockerComposeCredentials(
            DockerComposeConfig(
                List.of<String?>(interpreterConfig.composeFilePath),
                interpreterConfig.name
            )
        )
        val sdkData = this.buildSdkAdditionalData(interpreter, interpreterConfig, credentials)
        interpreter.setName(interpreterConfig.name)
        interpreter.setPhpSdkAdditionalData(sdkData)
        interpreter.setHomePath(sdkData.getSdkId())
    }

    private fun buildSdkAdditionalData(
        interpreter: PhpInterpreter,
        interpreterConfig: DdevInterpreterConfig,
        credentials: DockerComposeCredentialsHolder?
    ): PhpRemoteSdkAdditionalData {
        val sdkData = PhpRemoteSdkAdditionalData(interpreterConfig.phpVersion)
        sdkData.setInterpreterId(interpreter.getId())
        sdkData.setHelpersPath(PhpInterpreterProviderImpl.Companion.HELPERS_DIR)
        sdkData.setTypeData(PhpDockerComposeTypeData(PhpDockerComposeStartCommand.EXEC))
        sdkData.setCredentials<DockerComposeCredentialsHolder?>(
            DockerComposeCredentialsType.getInstance().getCredentialsKey(), credentials
        )
        sdkData.setPathMappings(this.loadPathMappings(sdkData))

        return sdkData
    }

    private fun loadPathMappings(sdkData: PhpRemoteSdkAdditionalData): PathMappingSettings? {
        val phpRemoteInterpreterManager = PhpRemoteInterpreterManager.getInstance()

        if (phpRemoteInterpreterManager != null) {
            return phpRemoteInterpreterManager.createPathMappings(this.project, sdkData)
        }

        return null
    }

    private fun loadPhpInfo(interpreter: PhpInterpreter) {
        val phpInfo = PhpInfoUtil.getPhpInfo(this.project, interpreter, null)
        PhpInterpretersPhpInfoCacheImpl.getInstance(this.project).setPhpInfo(interpreter.getName(), phpInfo)
    }

    private fun setDefaultIfNotSet(interpreter: PhpInterpreter) {
        val phpConfigurationFacade = PhpProjectConfigurationFacade.getInstance(this.project)
        val phpConfiguration = phpConfigurationFacade.getProjectConfiguration()

        if (phpConfiguration.getInterpreterName() == null) {
            phpConfiguration.setInterpreterName(interpreter.getName())
        }
    }

    companion object {
        private const val LEGACY_INTERPRETER_NAME = "DDEV"
        private const val HELPERS_DIR = "/opt/.phpstorm_helpers"
        private val LOG = Logger.getInstance(PhpInterpreterProviderImpl::class.java)
    }
}
