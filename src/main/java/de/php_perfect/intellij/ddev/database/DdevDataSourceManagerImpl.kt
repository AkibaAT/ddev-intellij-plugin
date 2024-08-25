package de.php_perfect.intellij.ddev.database

import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.database.util.LoaderContext
import com.intellij.database.util.performAutoIntrospection
import com.intellij.openapi.application.EDT
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.index.ManagedConfigurationIndex
import kotlinx.coroutines.*

class DdevDataSourceManagerImpl(private val project: Project) : DdevDataSourceManager {
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun updateDdevDataSource(dataSourceConfig: DataSourceConfig) {
        coroutineScope.launch {
            updateDataSourceAsync(dataSourceConfig)
        }
    }

    private suspend fun updateDataSourceAsync(dataSourceConfig: DataSourceConfig) {
        val hash = dataSourceConfig.hashCode()
        val managedConfigurationIndex = ManagedConfigurationIndex.getInstance(project)
        val indexEntry = managedConfigurationIndex[DataSourceConfig::class.java]

        val localDataSourceManager = withContext(Dispatchers.EDT) {
            LocalDataSourceManager.getInstance(project)
        }

        val localDataSource = withContext(Dispatchers.EDT) {
            val dataSources = localDataSourceManager.dataSources
            indexEntry?.let { entry ->
                dataSources.find { it.uniqueId == entry.id }
            } ?: dataSources.find { it.name == LEGACY_DATA_SOURCE_NAME }
            ?: localDataSourceManager.createEmpty().also { dataSources.add(it) }
        }

        if (indexEntry != null && localDataSource.uniqueId == indexEntry.id && indexEntry.hashEquals(hash)) {
            LOG.debug("Data source configuration ${dataSourceConfig.name} is up to date")
            return
        }

        LOG.debug("Updating data source configuration ${dataSourceConfig.name}")

        withContext(Dispatchers.EDT) {
            DataSourceProvider.getInstance().updateDataSource(localDataSource, dataSourceConfig)
            localDataSourceManager.fireDataSourceUpdated(localDataSource)
        }

        val loaderContext = withContext(Dispatchers.EDT) {
            LoaderContext.selectGeneralTask(project, localDataSource)
        }

        performAutoIntrospection(loaderContext, false)

        withContext(Dispatchers.EDT) {
            managedConfigurationIndex[localDataSource.uniqueId, DataSourceConfig::class.java] = hash
        }
    }

    override fun dispose() {
        coroutineScope.cancel()
    }

    companion object {
        private const val LEGACY_DATA_SOURCE_NAME = "DDEV"
        private val LOG = Logger.getInstance(DdevDataSourceManagerImpl::class.java)
    }
}