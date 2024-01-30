package org.apache.shardingsphere.single.util;

import org.apache.shardingsphere.infra.database.core.type.DatabaseType;
import org.apache.shardingsphere.infra.database.h2.type.H2DatabaseType;
import org.apache.shardingsphere.infra.database.testcontainers.type.TcPostgreSQLDatabaseType;
import org.apache.shardingsphere.infra.datanode.DataNode;
import org.apache.shardingsphere.single.api.constant.SingleTableConstants;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SingleTableLoadUtilsTest {

    @Test
    void assertSplitTableLines() {
        String tableOne = "foo,bar,foobar";
        String tableTwo = "waldo,fred";
        String tableThree = "johnDoe";
        String repeatedTable = "foo,waldo,foobar";
        Collection<String> result = SingleTableLoadUtils.splitTableLines(Arrays.asList(tableOne, tableTwo, tableThree, repeatedTable));
        assertEquals(6, result.size());
        Collection<String> expectedList = Arrays.asList("foo", "bar", "foobar", "waldo", "fred", "johnDoe");
        assertTrue(expectedList.size() == result.size() && expectedList.containsAll(result) && result.containsAll(expectedList));
    }

    @Test
    void assertConvertToDataNodes() {
        String fooTable = "foo.foo.foo";
        String barTable = "bar.bar";
        Collection<String> tables = Arrays.asList(fooTable, barTable);
        String databaseName = "fooDatabase";
        TcPostgreSQLDatabaseType postgreSQLDatabaseType = new TcPostgreSQLDatabaseType();
        Collection<DataNode> result = SingleTableLoadUtils.convertToDataNodes(databaseName, postgreSQLDatabaseType, tables);
        DataNode fooDataNode = new DataNode(databaseName, postgreSQLDatabaseType, fooTable);
        DataNode barDataNode = new DataNode(databaseName, postgreSQLDatabaseType, barTable);
        assertEquals(2, result.size());
        assertTrue(result.contains(fooDataNode));
        assertTrue(result.contains(barDataNode));
    }

    @Test
    void assertGetAllTablesNodeStrDefaultSchema() {
        TcPostgreSQLDatabaseType postgreSQLDatabaseType = new TcPostgreSQLDatabaseType();
        String result = SingleTableLoadUtils.getAllTablesNodeStr(postgreSQLDatabaseType);
        assertEquals(SingleTableConstants.ALL_SCHEMA_TABLES, result);
    }

    @Test
    void assertGetAllTablesNodeStrNoDefaultSchema() {
        H2DatabaseType h2DatabaseType = new H2DatabaseType();
        String result = SingleTableLoadUtils.getAllTablesNodeStr(h2DatabaseType);
        assertEquals(SingleTableConstants.ALL_TABLES, result);
    }

    @Test
    void assertGetAllTablesNodeStrFromDataSourceDefaultSchema() {
        TcPostgreSQLDatabaseType databaseType = new TcPostgreSQLDatabaseType();
        String dataSourceName = "foo";
        String schemaName = "bar";
        String result = SingleTableLoadUtils.getAllTablesNodeStrFromDataSource(databaseType, dataSourceName, schemaName);
        assertEquals("foo.bar.*", result);
    }

    @Test
    void assertGetAllTablesNodeStrFromDataNoSourceDefaultSchema() {

        H2DatabaseType databaseType = new H2DatabaseType();
        String dataSourceName = "foo";
        String schemaName = "bar";
        String result = SingleTableLoadUtils.getAllTablesNodeStrFromDataSource(databaseType, dataSourceName, schemaName);
        assertEquals("foo.*", result);
    }
}
