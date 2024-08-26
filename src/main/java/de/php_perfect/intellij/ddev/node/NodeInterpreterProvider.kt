package de.php_perfect.intellij.ddev.node

import com.intellij.openapi.project.Project

interface NodeInterpreterProvider {
    fun configureNodeInterpreter(nodeInterpreterConfig: NodeInterpreterConfig)

    companion object {
        fun getInstance(project: Project): NodeInterpreterProvider? {
            return project.getService<NodeInterpreterProvider?>(NodeInterpreterProvider::class.java)
        }
    }
}
