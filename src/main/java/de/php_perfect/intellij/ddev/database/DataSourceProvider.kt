package de.php_perfect.intellij.ddev.database

import com.intellij.database.dataSource.LocalDataSource
import com.intellij.openapi.application.ApplicationManager

interface DataSourceProvider {
    fun updateDataSource(dataSource: LocalDataSource, dataSourceConfig: DataSourceConfig)

    companion object {
        fun getInstance(): DataSourceProvider? {
            return ApplicationManager.getApplication().getService<DataSourceProvider?>(DataSourceProvider::class.java)
        }
    }
}
