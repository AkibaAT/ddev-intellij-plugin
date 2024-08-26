package de.php_perfect.intellij.ddev.database

import com.intellij.database.dataSource.LocalDataSource
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Exception

internal class DataSourceProviderTest : BasePlatformTestCase() {
    @BeforeEach
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
    }

    @AfterEach
    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun mySql() {
        val dataSourceConfig = DataSourceConfig(
            "DDEV",
            "Some Description",
            DataSourceConfig.Type.MYSQL,
            "8.0",
            "127.0.0.1",
            12345,
            "db",
            "some-user",
            "some-password"
        )

        val dataSource = LocalDataSource()
        DataSourceProviderImpl().updateDataSource(dataSource, dataSourceConfig)

        Assertions.assertInstanceOf<LocalDataSource?>(LocalDataSource::class.java, dataSource)
        Assertions.assertNotNull(dataSource)
        Assertions.assertNotNull(dataSource.getDatabaseDriver())
        Assertions.assertEquals("mysql.8", dataSource.getDatabaseDriver()!!.getId())
        Assertions.assertEquals(
            "jdbc:mysql://127.0.0.1:12345/db?user=some-user&password=some-password",
            dataSource.getUrl()
        )
    }

    @Test
    fun mySql56() {
        val dataSourceConfig = DataSourceConfig(
            "DDEV",
            "Some Description",
            DataSourceConfig.Type.MYSQL,
            "5.6",
            "127.0.0.1",
            12345,
            "db",
            "some-user",
            "some-password"
        )

        val dataSource = LocalDataSource()
        DataSourceProviderImpl().updateDataSource(dataSource, dataSourceConfig)

        Assertions.assertInstanceOf<LocalDataSource?>(LocalDataSource::class.java, dataSource)
        Assertions.assertNotNull(dataSource)
        Assertions.assertNotNull(dataSource.getDatabaseDriver())
        Assertions.assertEquals("mysql.8", dataSource.getDatabaseDriver()!!.getId())
        Assertions.assertEquals(
            "jdbc:mysql://127.0.0.1:12345/db?user=some-user&password=some-password",
            dataSource.getUrl()
        )
    }

    fun mySql51() {
        val dataSourceConfig = DataSourceConfig(
            "DDEV",
            "Some Description",
            DataSourceConfig.Type.MYSQL,
            "5.1",
            "127.0.0.1",
            12345,
            "db",
            "some-user",
            "some-password"
        )

        val dataSource = LocalDataSource()
        DataSourceProviderImpl().updateDataSource(dataSource, dataSourceConfig)

        Assertions.assertInstanceOf<LocalDataSource?>(LocalDataSource::class.java, dataSource)
        Assertions.assertNotNull(dataSource)
        Assertions.assertNotNull(dataSource.getDatabaseDriver())
        Assertions.assertEquals("mysql", dataSource.getDatabaseDriver()!!.getId())
        Assertions.assertEquals(
            "jdbc:mysql://127.0.0.1:12345/db?user=some-user&password=some-password",
            dataSource.getUrl()
        )
    }

    @Test
    fun mariaDb() {
        val dataSourceConfig = DataSourceConfig(
            "DDEV",
            "Some Description",
            DataSourceConfig.Type.MARIADB,
            "10.4",
            "127.0.0.1",
            12345,
            "db",
            "some-user",
            "some-password"
        )

        val dataSource = LocalDataSource()
        DataSourceProviderImpl().updateDataSource(dataSource, dataSourceConfig)

        Assertions.assertInstanceOf<LocalDataSource?>(LocalDataSource::class.java, dataSource)
        Assertions.assertNotNull(dataSource)
        Assertions.assertEquals(
            "jdbc:mariadb://127.0.0.1:12345/db?user=some-user&password=some-password",
            dataSource.getUrl()
        )
    }

    @Test
    fun postgreSql() {
        val dataSourceConfig = DataSourceConfig(
            "DDEV",
            "Some Description",
            DataSourceConfig.Type.POSTGRESQL,
            "15.5",
            "127.0.0.1",
            12345,
            "db",
            "some-user",
            "some-password"
        )

        val dataSource = LocalDataSource()
        DataSourceProviderImpl().updateDataSource(dataSource, dataSourceConfig)

        Assertions.assertInstanceOf<LocalDataSource?>(LocalDataSource::class.java, dataSource)
        Assertions.assertNotNull(dataSource)
        Assertions.assertEquals(
            "jdbc:postgresql://127.0.0.1:12345/db?user=some-user&password=some-password",
            dataSource.getUrl()
        )
    }
}
