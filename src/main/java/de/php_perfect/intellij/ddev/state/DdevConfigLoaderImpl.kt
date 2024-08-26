package de.php_perfect.intellij.ddev.state

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import java.nio.file.Paths

class DdevConfigLoaderImpl(project: Project) : DdevConfigLoader {
    private val project: Project

    init {
        this.project = project
    }

    override fun load(): VirtualFile? {
        val basePath = this.project.getBasePath()

        if (basePath == null) {
            return null
        }

        val path = Paths.get(basePath, DdevConfigLoaderImpl.Companion.DDEV_CONFIG_PATH)

        val config = VirtualFileManager.getInstance().refreshAndFindFileByNioPath(path)

        if (config == null) {
            return null
        }

        config.refresh(false, false)

        return config
    }

    override fun exists(): Boolean {
        val ddevConfig = this.load()

        return ddevConfig != null && ddevConfig.exists()
    }

    companion object {
        private const val DDEV_CONFIG_PATH = ".ddev/config.yaml"
    }
}
