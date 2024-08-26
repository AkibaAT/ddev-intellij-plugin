package de.php_perfect.intellij.ddev.database

import com.intellij.database.dataSource.LocalDataSource
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.database.util.LoaderContext
import com.intellij.database.util.performAutoIntrospection
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.index.ManagedConfigurationIndex
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class DdevDataSourceManagerImpl(project: Project) : DdevDataSourceManager {
    private val project: Project

    init {
        this.project = project
    }

    override fun updateDdevDataSource(dataSourceConfig: DataSourceConfig) {
        val hash = dataSourceConfig.hashCode()
        val managedConfigurationIndex = ManagedConfigurationIndex.getInstance(this.project)
        val indexEntry = managedConfigurationIndex.get(DataSourceConfig::class.java)

        val localDataSourceManager = LocalDataSourceManager.getInstance(this.project)
        val dataSources = localDataSourceManager.getDataSources()

        var localDataSource: LocalDataSource? = null
        if (indexEntry != null && (dataSources.stream()
                .filter { currentDataSource: LocalDataSource? -> currentDataSource!!.getUniqueId() == indexEntry.id }
                .findFirst()
                .orElse(null).also { localDataSource = it }) != null && indexEntry.hashEquals(hash)
        ) {
            DdevDataSourceManagerImpl.Companion.LOG.debug(
                String.format(
                    "Data source configuration %s is up to date",
                    dataSourceConfig.name
                )
            )
            return
        }

        DdevDataSourceManagerImpl.Companion.LOG.debug(
            String.format(
                "Updating data source configuration %s",
                dataSourceConfig.name
            )
        )

        if (localDataSource == null) {
            localDataSource = dataSources.stream()
                .filter { currentDataSource: LocalDataSource? -> currentDataSource!!.getName() == DdevDataSourceManagerImpl.Companion.LEGACY_DATA_SOURCE_NAME }
                .findFirst()
                .orElse(null)
        }

        if (localDataSource == null) {
            localDataSource = localDataSourceManager.createEmpty()
            dataSources.add(localDataSource)
        }

        val dataSource: LocalDataSource? = localDataSource
        DataSourceProvider.Companion.getInstance().updateDataSource(dataSource, dataSourceConfig)

        ApplicationManager.getApplication().invokeLater(Runnable {
            localDataSourceManager.fireDataSourceUpdated(dataSource!!)
            val loaderContext = LoaderContext.selectGeneralTask(project, dataSource)
            performAutoIntrospection(loaderContext, false, object : Continuation<SyncResult?> {
                override fun getContext(): CoroutineContext {
                    return@invokeLater EmptyCoroutineContext
                }

                override fun resumeWith(o: Any) {
                }
            })
        })

        managedConfigurationIndex.set(dataSource!!.getUniqueId(), DataSourceConfig::class.java, hash)
    }

    companion object {
        private const val LEGACY_DATA_SOURCE_NAME = "DDEV"
        private val LOG = Logger.getInstance(DdevDataSourceManagerImpl::class.java)
    }
}
