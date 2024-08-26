package de.php_perfect.intellij.ddev.database

import com.intellij.database.Dbms
import com.intellij.database.dataSource.DatabaseDriver
import com.intellij.database.dataSource.DatabaseDriverManager
import com.intellij.database.dataSource.LocalDataSource
import com.intellij.database.dataSource.SchemaControl
import com.intellij.database.introspection.DBIntrospectionConsts
import com.intellij.database.introspection.DBIntrospectionOptions.SourceLoading
import com.intellij.database.introspection.supportsMultilevelIntrospection
import com.intellij.database.model.properties.Level
import com.intellij.database.util.TreePatternUtils
import org.apache.maven.artifact.versioning.ComparableVersion

class DataSourceProviderImpl : DataSourceProvider {
    override fun updateDataSource(dataSource: LocalDataSource, dataSourceConfig: DataSourceConfig) {
        val driver = this.getDriverByDatabaseType(dataSourceConfig.type, dataSourceConfig.version)
        val dsn = DataSourceProviderImpl.Companion.getDsnByDatabaseType(dataSourceConfig)

        dataSource.setUrlSmart(dsn)
        dataSource.setDatabaseDriver(driver)
        dataSource.setPasswordStorage(LocalDataSource.Storage.PERSIST)
        dataSource.setName(dataSourceConfig.name)
        dataSource.setComment(dataSourceConfig.description)
        dataSource.setUsername(dataSourceConfig.username)
        dataSource.setSchemaControl(SchemaControl.AUTOMATIC)
        dataSource.setAutoSynchronize(true)
        dataSource.setCheckOutdated(true)
        dataSource.setSourceLoading(SourceLoading.USER_AND_SYSTEM_SOURCES)

        val dbms = Dbms.forConnection(dataSource)
        val treePattern = TreePatternUtils.importPattern(dbms, DBIntrospectionConsts.ALL_NAMESPACES)
        dataSource.setIntrospectionScope(treePattern)
        dataSource.setIntrospectionLevel(if (dbms.supportsMultilevelIntrospection) Level.L3 else null)
    }

    private fun getDriverByDatabaseType(databaseType: DataSourceConfig.Type, version: String): DatabaseDriver {
        val databaseDriverManager = DatabaseDriverManager.getInstance()

        return when (databaseType) {
            DataSourceConfig.Type.POSTGRESQL -> databaseDriverManager.getDriver("postgresql")
            DataSourceConfig.Type.MARIADB -> databaseDriverManager.getDriver("mariadb")
            DataSourceConfig.Type.MYSQL -> if (ComparableVersion(version).compareTo(ComparableVersion("5.2")) < 0) databaseDriverManager.getDriver(
                "mysql"
            ) else databaseDriverManager.getDriver("mysql.8")
        }
    }

    companion object {
        private fun getDsnByDatabaseType(databaseInfo: DataSourceConfig): String {
            return String.format(
                "jdbc:%s://%s:%d/%s?user=%s&password=%s",
                DataSourceProviderImpl.Companion.getDsnType(databaseInfo.type),
                databaseInfo.host,
                databaseInfo.port,
                databaseInfo.database,
                databaseInfo.username,
                databaseInfo.password
            )
        }

        private fun getDsnType(databaseType: DataSourceConfig.Type): String {
            return when (databaseType) {
                DataSourceConfig.Type.POSTGRESQL -> "postgresql"
                DataSourceConfig.Type.MARIADB -> "mariadb"
                DataSourceConfig.Type.MYSQL -> "mysql"
            }
        }
    }
}
