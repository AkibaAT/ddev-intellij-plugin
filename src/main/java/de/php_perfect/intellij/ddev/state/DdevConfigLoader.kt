package de.php_perfect.intellij.ddev.state

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

interface DdevConfigLoader {
    fun exists(): Boolean

    fun load(): VirtualFile?

    companion object {
        @JvmStatic
        fun getInstance(project: Project): DdevConfigLoader? {
            return project.getService<DdevConfigLoader?>(DdevConfigLoader::class.java)
        }
    }
}
