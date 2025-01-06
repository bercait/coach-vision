package pt.bemanos.sports.coachvision.database;

import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;

import java.nio.file.Path;

public class DatabaseFactory {
    private static final DatabaseManagementService managementService = new DatabaseManagementServiceBuilder(
            Path.of("target", "db", "neo4jg")).build();
    private static final GraphDatabaseService graphDB = managementService.database("neo4j");
    private static final DatabaseFactory databaseFactory = new DatabaseFactory();

    private DatabaseFactory() {
        // Registers a shutdown hook for the Neo4j instance so that it shuts down nicely when the VM exits
        // (even if you "Ctrl-C" the running application).
        Runtime.getRuntime().addShutdownHook(new Thread(managementService::shutdown));

    }

    public static DatabaseFactory getInstance() {
        return databaseFactory;
    }

    public GraphDatabaseService getGraphDatabase() {
        return graphDB;
    }
}
