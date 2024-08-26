package de.php_perfect.intellij.ddev.dockerCompose

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

interface DdevComposeFileLoader {
    fun load(): VirtualFile?

    companion object {
        @JvmStatic
        fun getInstance(project: Project): DdevComposeFileLoader? {
            return project.getService<DdevComposeFileLoader?>(DdevComposeFileLoader::class.java)
        }
    }
}
