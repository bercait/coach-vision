package pt.bemanos.sports.coachvision.database;

import org.neo4j.configuration.connectors.BoltConnector;
import org.neo4j.configuration.helpers.SocketAddress;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;

import java.nio.file.Path;

public class DatabaseFactory {
    private static final DatabaseManagementService managementService = new DatabaseManagementServiceBuilder(
            Path.of("target", "db", "neo4j"))
            .setConfig(BoltConnector.enabled, true)
            .setConfig(BoltConnector.listen_address, new SocketAddress("localhost", 7687))
            .build();
    private static final GraphDatabaseService graphDB = managementService.database("neo4j");
    private static final DatabaseFactory databaseFactory = new DatabaseFactory();

    private DatabaseFactory() {
    }

    public static DatabaseFactory getInstance() {
        return databaseFactory;
    }

    public GraphDatabaseService getGraphDatabase() {
        return graphDB;
    }

    public DatabaseManagementService getManagementService() {
        return managementService;
    }
}
