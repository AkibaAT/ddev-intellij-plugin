package de.php_perfect.intellij.ddev.node

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.DescriptionChangedListener
import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.dockerCompose.DdevComposeFileLoader.Companion.getInstance
import de.php_perfect.intellij.ddev.dockerCompose.DockerComposeCredentialProvider.Companion.getInstance
import de.php_perfect.intellij.ddev.settings.DdevSettingsState

class AutoConfigureNodeInterpreterListener(project: Project) : DescriptionChangedListener {
    private val project: Project

    init {
        this.project = project
    }

    override fun onDescriptionChanged(description: Description?) {
        if (description == null || description.getName() == null) {
            return
        }

        if (!DdevSettingsState.getInstance(this.project).autoConfigureNodeJsInterpreter) {
            return
        }

        val composeFile = getInstance(this.project)!!.load()

        if (composeFile == null || !composeFile.exists()) {
            return
        }

        val nodeInterpreterConfig = NodeInterpreterConfig(description.getName()!!, composeFile.getPath(), "node")
        NodeInterpreterProvider.Companion.getInstance(this.project).configureNodeInterpreter(nodeInterpreterConfig)
    }
}
