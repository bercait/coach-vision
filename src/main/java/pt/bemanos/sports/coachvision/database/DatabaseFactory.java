package pt.bemanos.sports.coachvision.database;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

public class DatabaseFactory {
    private static final Configuration CONFIGURATION = new Configuration.Builder().uri("bolt://localhost:7687").build();
    private static final SessionFactory SESSION_FACTORY = new SessionFactory(CONFIGURATION, "pt.bemanos.sports.coachvision.domain");
    private static final DatabaseFactory DATABASE_FACTORY = new DatabaseFactory();
    private static Session SESSION;

    private DatabaseFactory() {
    }

    public static DatabaseFactory getInstance() {
        return DATABASE_FACTORY;
    }

    public SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public Session getSession() {
        if (SESSION == null) {
            SESSION = SESSION_FACTORY.openSession();
        }

        return SESSION;
    }
}
