package pt.bemanos.sports.coachvision.database;

import org.neo4j.configuration.connectors.BoltConnector;
import org.neo4j.configuration.helpers.SocketAddress;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;

import java.nio.file.Path;

public class ServerFactory {
    private static final DatabaseManagementService MANAGEMENT_SERVICE = new DatabaseManagementServiceBuilder(
            Path.of("target", "db", "neo4j"))
            .setConfig(BoltConnector.enabled, true)
            .setConfig(BoltConnector.listen_address, new SocketAddress("localhost", 7687))
            .build();
    private static final GraphDatabaseService GRAPH_DB = MANAGEMENT_SERVICE.database("neo4j");
    private static final ServerFactory SERVER_FACTORY = new ServerFactory();

    private ServerFactory() {
    }

    public static ServerFactory getInstance() {
        return SERVER_FACTORY;
    }

    public GraphDatabaseService getGraphDatabase() {
        return GRAPH_DB;
    }

    public DatabaseManagementService getManagementService() {
        return MANAGEMENT_SERVICE;
    }
}
