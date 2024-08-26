package de.php_perfect.intellij.ddev.database

import com.intellij.openapi.project.Project

interface DdevDataSourceManager {
    fun updateDdevDataSource(dataSourceConfig: DataSourceConfig)

    companion object {
        fun getInstance(project: Project): DdevDataSourceManager? {
            return project.getService<DdevDataSourceManager?>(DdevDataSourceManager::class.java)
        }
    }
}
