package uk.gov.justice.services.healthcheck.utils.database;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.gov.justice.services.test.utils.core.jdbc.LiquibaseDatabaseBootstrapper;
import uk.gov.justice.services.test.utils.persistence.TestJdbcDataSourceProvider;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class DatabaseTableListerIT {

    private static final String LIQUIBASE_CHANGELOG_XML = "liquibase/framework-system-changelog.xml";

    private final TestJdbcDataSourceProvider testJdbcDataSourceProvider = new TestJdbcDataSourceProvider();

    private DatabaseTableLister databaseTableLister = new DatabaseTableLister();


    private final LiquibaseDatabaseBootstrapper liquibaseDatabaseBootstrapper = new LiquibaseDatabaseBootstrapper();


    @BeforeEach
    public void setupEventstoreDatabase() throws Exception {

        try (final Connection connection = testJdbcDataSourceProvider.getSystemDataSource("framework").getConnection()) {
            liquibaseDatabaseBootstrapper.bootstrap(
                    LIQUIBASE_CHANGELOG_XML,
                    connection);
        }
    }

    @Test
    public void shouldListAllTablesForGivenDatasourceExcludingLiquibaseTables() throws Exception {

        final DataSource eventStoreDataSource = testJdbcDataSourceProvider.getSystemDataSource("framework");
        final List<String> tableNames = databaseTableLister.listTables(eventStoreDataSource);

        System.out.println(tableNames);

        assertThat(tableNames.size(), is(2));

        assertThat(tableNames, hasItem("stored_command"));
        assertThat(tableNames, hasItem("system_command_status"));
    }
}