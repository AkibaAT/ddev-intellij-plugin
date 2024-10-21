package de.php_perfect.intellij.ddev.database

import com.intellij.database.dataSource.LocalDataSource
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.database.util.DataSourceUtilKt
import com.intellij.database.util.LoaderContext
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.index.IndexEntry
import de.php_perfect.intellij.ddev.index.ManagedConfigurationIndex
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class DdevDataSourceManagerImpl(private val project: Project) : DdevDataSourceManager {
    override fun updateDdevDataSource(dataSourceConfig: DataSourceConfig) {
        val hash = dataSourceConfig.hashCode()
        val managedConfigurationIndex = ManagedConfigurationIndex.getInstance(project)
        val indexEntry = managedConfigurationIndex.get(DataSourceConfig::class.java)

        val localDataSourceManager = LocalDataSourceManager.getInstance(project)
        val dataSources = localDataSourceManager.dataSources

        var localDataSource: LocalDataSource? = null
        if (indexEntry != null && dataSources.find { it.uniqueId == indexEntry.id }?.also { localDataSource = it } != null && indexEntry.hashEquals(hash)) {
            LOG.debug("Data source configuration ${dataSourceConfig.name} is up to date")
            return
        }

        LOG.debug("Updating data source configuration ${dataSourceConfig.name}")

        if (localDataSource == null) {
            localDataSource = dataSources.find { it.name == LEGACY_DATA_SOURCE_NAME }
        }

        if (localDataSource == null) {
            localDataSource = localDataSourceManager.createEmpty()
            dataSources.add(localDataSource)
        }

        val dataSource = localDataSource
        DataSourceProvider.getInstance().updateDataSource(dataSource, dataSourceConfig)

        ApplicationManager.getApplication().invokeLater {
            localDataSourceManager.fireDataSourceUpdated(dataSource)
            val loaderContext = LoaderContext.selectGeneralTask(project, dataSource)
            DataSourceUtilKt.performAutoIntrospection(loaderContext, false, object : Continuation<Unit> {
                override val context: CoroutineContext
                    get() = EmptyCoroutineContext

                override fun resumeWith(result: Result<Unit>) {
                    // Handle result if needed
                }
            })
        }

        managedConfigurationIndex.set(dataSource.uniqueId, DataSourceConfig::class.java, hash)
    }

    companion object {
        private const val LEGACY_DATA_SOURCE_NAME = "DDEV"
        private val LOG = Logger.getInstance(DdevDataSourceManagerImpl::class.java)
    }
}
