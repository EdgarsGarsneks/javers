package org.javers.repository.sql.schema;

import java.util.Optional;

public abstract class SchemaNameAware {
    private final TableNameProvider tableNameProvider;

    protected SchemaNameAware(TableNameProvider tableNameProvider) {
        this.tableNameProvider = tableNameProvider;
    }

    protected String getCommitTableNameWithSchema() {
        return tableNameProvider.getCommitTableNameWithSchema();
    }

    protected String getSnapshotTableNameWithSchema() {
        return tableNameProvider.getSnapshotTableNameWithSchema();
    }

    protected String getGlobalIdTableNameWithSchema() {
        return tableNameProvider.getGlobalIdTableNameWithSchema();
    }

    protected String getCommitPropertyTableNameWithSchema() {
        return tableNameProvider.getCommitPropertyTableNameWithSchema();
    }

    protected DBObjectName getGlobalIdTableName() {
        return tableNameProvider.getGlobalIdTableName();
    }

    protected DBObjectName getCommitTableName() {
        return tableNameProvider.getCommitTableName();
    }

    protected DBObjectName getCommitPropertyTableName() {
        return tableNameProvider.getCommitPropertyTableName();
    }

    protected DBObjectName getSnapshotTableName() {
        return tableNameProvider.getSnapshotTableName();
    }

    protected String getCommitPkSeqWithSchema(){
        return tableNameProvider.getCommitPkSeqWithSchema();
    }

    protected String getSnapshotTablePkSeqWithSchema(){
        return tableNameProvider.getSnapshotTablePkSeqWithSchema();
    }

    protected String getGlobalIdPkSeqWithSchema() {
        return tableNameProvider.getGlobalIdPkSeqWithSchema();
    }

    protected Optional<String> getSchemaName() {
        return tableNameProvider.getSchemaName();
    }
}
