package de.php_perfect.intellij.ddev.database

import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.DatabaseInfoChangedListener
import de.php_perfect.intellij.ddev.cmd.DatabaseInfo
import de.php_perfect.intellij.ddev.settings.DdevSettingsState

class AutoConfigureDataSourceListener(project: Project) : DatabaseInfoChangedListener {
    private val project: Project


    init {
        this.project = project
    }

    override fun onDatabaseInfoChanged(databaseInfo: DatabaseInfo?) {
        if (databaseInfo == null) {
            return
        }

        if (!DdevSettingsState.getInstance(this.project).autoConfigureDataSource) {
            return
        }

        if (databaseInfo.type == null || databaseInfo.version == null || databaseInfo.name == null || databaseInfo.username == null || databaseInfo.password == null) {
            return
        }

        val type = when (databaseInfo.type) {
            DatabaseInfo.Type.MYSQL -> DataSourceConfig.Type.MYSQL
            DatabaseInfo.Type.MARIADB -> DataSourceConfig.Type.MARIADB
            DatabaseInfo.Type.POSTGRESQL -> DataSourceConfig.Type.POSTGRESQL
        }

        DdevDataSourceManager.Companion.getInstance(this.project).updateDdevDataSource(
            DataSourceConfig(
                "DDEV",
                "DDEV generated data source",
                type,
                databaseInfo.version!!,
                AutoConfigureDataSourceListener.Companion.HOST,
                databaseInfo.publishedPort,
                databaseInfo.name!!,
                databaseInfo.username!!,
                databaseInfo.password!!
            )
        )
    }

    companion object {
        private const val HOST = "127.0.0.1"
    }
}
