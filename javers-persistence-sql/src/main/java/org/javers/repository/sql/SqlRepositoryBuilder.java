package org.javers.repository.sql;

import org.javers.core.AbstractContainerBuilder;
import org.javers.repository.sql.pico.JaversSqlModule;
import org.javers.repository.sql.session.SessionFactory;
import org.polyjdbc.core.PolyJDBC;
import org.polyjdbc.core.PolyJDBCBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author bartosz walacik
 */
public class SqlRepositoryBuilder extends AbstractContainerBuilder {
    private static final Logger logger = LoggerFactory.getLogger(SqlRepositoryBuilder.class);

    private DialectName dialectName;
    private ConnectionProvider connectionProvider;

    private String schemaName;
    private boolean globalIdCacheDisabled;
    private boolean schemaManagementEnabled = true;

    /**
     * table names to be used when creating Javers SQL schema
     */
    private String globalIdTableName;
    private String commitTableName;
    private String snapshotTableName;
    private String commitPropertyTableName;

    public SqlRepositoryBuilder() {
    }

    public static SqlRepositoryBuilder sqlRepository() {
        return new SqlRepositoryBuilder();
    }

    public SqlRepositoryBuilder withDialect(DialectName dialect) {
        dialectName = dialect;
        return this;
    }

    public SqlRepositoryBuilder withConnectionProvider(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        return this;
    }

    /**
     * This function sets a schema to be used for creation and updating tables. When passing a schema name make sure
     * that the schema has been created in the database before running JaVers. If schemaName is null or empty, the default
     * schema is used instead.
     *
     * @since 2.4
     */
    public SqlRepositoryBuilder withSchema(String schemaName) {
        if (schemaName != null && !schemaName.isEmpty()) {
            this.schemaName = schemaName;
        }
        return this;
    }

    /**
     * Since 2.7.2, JaversTransactionalDecorator evicts the cache on transaction rollback,
     * so there are no known reasons to disabling it.
     */
    public SqlRepositoryBuilder withGlobalIdCacheDisabled(boolean globalIdCacheDisabled) {
        this.globalIdCacheDisabled = globalIdCacheDisabled;
        return this;
    }

    public SqlRepositoryBuilder withSchemaManagementEnabled(boolean schemaManagementEnabled){
        this.schemaManagementEnabled = schemaManagementEnabled;
        return this;
    }

    public SqlRepositoryBuilder withGlobalIdTableName(String globalIdTableName) {
        if(globalIdTableName !=null && !globalIdTableName.isEmpty()) {
            this.globalIdTableName = globalIdTableName;
        }
        return this;
    }

    public SqlRepositoryBuilder withCommitTableName(String commitTableName) {
        if(commitTableName !=null && !commitTableName.isEmpty()) {
            this.commitTableName = commitTableName;
        }
        return this;
    }

    public SqlRepositoryBuilder withSnapshotTableName(String snapshotTableName) {
        if(snapshotTableName !=null && !snapshotTableName.isEmpty()) {
            this.snapshotTableName = snapshotTableName;
        }
        return this;
    }

    public SqlRepositoryBuilder withCommitPropertyTableName(String commitPropertyTableName) {
        if(commitPropertyTableName !=null && !commitPropertyTableName.isEmpty()) {
            this.commitPropertyTableName = commitPropertyTableName;
        }
        return this;
    }

    public JaversSqlRepository build() {
        logger.info("starting SqlRepository...");
        logger.info("  dialect:                 {}", dialectName);
        logger.info("  schemaManagementEnabled: {}", schemaManagementEnabled);
        logger.info("  schemaName:              {}", schemaName);
        logger.info("  globalIdTableName        {}", globalIdTableName);
        logger.info("  commitTableName          {}", commitTableName);
        logger.info("  snapshotTableName        {}", snapshotTableName);
        logger.info("  commitPropertyTableName  {}", commitPropertyTableName);
        bootContainer();

        SqlRepositoryConfiguration config =
                new SqlRepositoryConfiguration(globalIdCacheDisabled, schemaName, schemaManagementEnabled,
                        globalIdTableName, commitTableName, snapshotTableName, commitPropertyTableName);
        addComponent(config);

        PolyJDBC polyJDBC = PolyJDBCBuilder.polyJDBC(dialectName.getPolyDialect(), config.getSchemaName())
                .usingManagedConnections(() -> connectionProvider.getConnection()).build();

        SessionFactory sessionFactory = new SessionFactory(dialectName, connectionProvider);

        addComponent(polyJDBC);
        addComponent(sessionFactory);

        addModule(new JaversSqlModule());

        addComponent(dialectName.getPolyDialect());
        addComponent(connectionProvider);
        return getContainerComponent(JaversSqlRepository.class);
    }

    /**
     * For testing only
     */
    @Override
    protected <T> T getContainerComponent(Class<T> ofClass) {
        return super.getContainerComponent(ofClass);
    }
}
