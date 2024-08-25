package de.php_perfect.intellij.ddev.database

import com.intellij.openapi.project.Project

interface DdevDataSourceManager {
    fun updateDdevDataSource(dataSourceConfig: DataSourceConfig)

    fun dispose()

    companion object {
        @JvmStatic
        fun getInstance(project: Project): DdevDataSourceManager {
            return project.getService(DdevDataSourceManager::class.java)
        }
    }
}
