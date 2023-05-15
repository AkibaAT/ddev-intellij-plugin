package de.php_perfect.intellij.ddev.database;

import com.intellij.database.dataSource.LocalDataSource;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import de.php_perfect.intellij.ddev.cmd.DatabaseInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class DataSourceProviderTest extends BasePlatformTestCase {
    @Override
    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    @AfterEach
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    void mySql() {
        DatabaseInfo databaseInfo = new DatabaseInfo(DatabaseInfo.Type.MYSQL, "8.0", 533, "", "some-internal-host", "some-user", "some-password", 12345);

        DataSourceProviderImpl dataSourceProvider = new DataSourceProviderImpl();

        LocalDataSource dataSource = dataSourceProvider.buildDdevDataSource(databaseInfo);
        Assertions.assertInstanceOf(LocalDataSource.class, dataSource);
        Assertions.assertNotNull(dataSource);
        Assertions.assertEquals("jdbc:mysql://127.0.0.1:12345/?user=some-user&password=some-password", dataSource.getUrl());
    }

    @Test
    void mariaDb() {
        DatabaseInfo databaseInfo = new DatabaseInfo(DatabaseInfo.Type.MARIADB, "10.4", 533, "", "some-internal-host", "some-user", "some-password", 12345);

        DataSourceProviderImpl dataSourceProvider = new DataSourceProviderImpl();

        LocalDataSource dataSource = dataSourceProvider.buildDdevDataSource(databaseInfo);
        Assertions.assertInstanceOf(LocalDataSource.class, dataSource);
        Assertions.assertNotNull(dataSource);
        Assertions.assertEquals("jdbc:mariadb://127.0.0.1:12345/?user=some-user&password=some-password", dataSource.getUrl());
    }

    @Test
    void postgreSql() {
        DatabaseInfo databaseInfo = new DatabaseInfo(DatabaseInfo.Type.POSTGRESQL, "8.0", 533, "", "some-internal-host", "some-user", "some-password", 12345);

        DataSourceProviderImpl dataSourceProvider = new DataSourceProviderImpl();

        LocalDataSource dataSource = dataSourceProvider.buildDdevDataSource(databaseInfo);
        Assertions.assertInstanceOf(LocalDataSource.class, dataSource);
        Assertions.assertNotNull(dataSource);
        Assertions.assertEquals("jdbc:postgresql://127.0.0.1:12345/?user=some-user&password=some-password", dataSource.getUrl());
    }
}
