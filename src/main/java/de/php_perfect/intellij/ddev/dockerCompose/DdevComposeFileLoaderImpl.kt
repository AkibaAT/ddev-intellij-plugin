package de.php_perfect.intellij.ddev.dockerCompose

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import java.nio.file.Paths

class DdevComposeFileLoaderImpl(project: Project) : DdevComposeFileLoader {
    private val project: Project

    init {
        this.project = project
    }

    override fun load(): VirtualFile? {
        val basePath = this.project.getBasePath()

        if (basePath == null) {
            return null
        }

        val path = Paths.get(basePath, DdevComposeFileLoaderImpl.Companion.DDEV_COMPOSE_PATH)

        return VirtualFileManager.getInstance().refreshAndFindFileByNioPath(path)
    }

    companion object {
        private const val DDEV_COMPOSE_PATH = ".ddev/.ddev-docker-compose-full.yaml"
    }
}
