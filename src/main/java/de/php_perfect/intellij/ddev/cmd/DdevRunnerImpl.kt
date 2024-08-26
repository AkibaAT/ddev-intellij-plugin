package de.php_perfect.intellij.ddev.cmd

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.PtyCommandLine
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.DdevConfigArgumentProvider
import de.php_perfect.intellij.ddev.DdevIntegrationBundle
import de.php_perfect.intellij.ddev.state.DdevConfigLoader
import de.php_perfect.intellij.ddev.state.DdevStateManager
import java.nio.charset.StandardCharsets
import java.util.List
import java.util.Objects

class DdevRunnerImpl : DdevRunner {
    override fun start(project: Project) {
        val title = DdevIntegrationBundle.message("ddev.run.start")
        val runner = Runner.getInstance(project)
        runner?.run(this.createCommandLine("start", project), title, Runnable { this.updateDescription(project) })
    }

    override fun restart(project: Project) {
        val title = DdevIntegrationBundle.message("ddev.run.restart")
        val runner = Runner.getInstance(project)
        runner?.run(this.createCommandLine("restart", project), title, Runnable { this.updateDescription(project) })
    }

    override fun stop(project: Project) {
        val title = DdevIntegrationBundle.message("ddev.run.stop")
        val runner = Runner.getInstance(project)
        runner?.run(this.createCommandLine("stop", project), title, Runnable { this.updateDescription(project) })
    }

    override fun powerOff(project: Project) {
        val title = DdevIntegrationBundle.message("ddev.run.powerOff")
        val runner = Runner.getInstance(project)
        runner?.run(this.createCommandLine("poweroff", project), title, Runnable { this.updateDescription(project) })
    }

    override fun delete(project: Project) {
        val title = DdevIntegrationBundle.message("ddev.run.delete")
        val runner = Runner.getInstance(project)
        runner?.run(this.createCommandLine("delete", project), title, Runnable { this.updateDescription(project) })
    }

    override fun share(project: Project) {
        val title = DdevIntegrationBundle.message("ddev.run.share")
        val runner = Runner.getInstance(project)
        runner?.run(this.createCommandLine("share", project), title)
    }

    override fun config(project: Project) {
        val title = DdevIntegrationBundle.message("ddev.run.config")
        val runner = Runner.getInstance(project)
        runner?.run(this.buildConfigCommandLine(project), title, Runnable {
            this.updateConfiguration(project)
            this.openConfig(project)
        })
    }

    private fun openConfig(project: Project) {
        val ddevConfig = DdevConfigLoader.getInstance(project)?.load()

        if (ddevConfig != null && ddevConfig.exists()) {
            FileEditorManager.getInstance(project).openFile(ddevConfig, true)
        }
    }

    private fun updateDescription(project: Project) {
        ApplicationManager.getApplication()
            .executeOnPooledThread(Runnable { DdevStateManager.getInstance(project).updateDescription() })
    }

    private fun updateConfiguration(project: Project) {
        ApplicationManager.getApplication()
            .executeOnPooledThread(Runnable { DdevStateManager.getInstance(project).updateConfiguration() })
    }

    private fun buildConfigCommandLine(project: Project): GeneralCommandLine {
        val commandLine = this.createCommandLine("config", project)
            .withParameters("--auto")

        for (ddevConfigArgumentProvider in CONFIG_ARGUMENT_PROVIDER_EP.extensionList) {
            commandLine.addParameters(ddevConfigArgumentProvider.getAdditionalArguments(project))
        }

        return commandLine
    }

    private fun createCommandLine(ddevAction: String, project: Project): GeneralCommandLine {
        val state = DdevStateManager.getInstance(project).getState()

        return PtyCommandLine(List.of<String?>(Objects.requireNonNull<String?>(state.getDdevBinary()), ddevAction))
            .withInitialRows(30)
            .withInitialColumns(120)
            .withWorkDirectory(project.basePath)
            .withCharset(StandardCharsets.UTF_8)
            .withEnvironment("DDEV_NONINTERACTIVE", "true")
    }

    companion object {
        private val CONFIG_ARGUMENT_PROVIDER_EP: ExtensionPointName<DdevConfigArgumentProvider> =
            create.create<DdevConfigArgumentProvider?>("de.php_perfect.intellij.ddev.ddevConfigArgumentProvider")
    }
}
