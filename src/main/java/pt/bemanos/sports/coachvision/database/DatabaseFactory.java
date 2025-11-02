package pt.bemanos.sports.coachvision.database;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

public class DatabaseFactory {
    private static final Configuration CONFIGURATION = new Configuration.Builder()
            .uri("bolt://localhost:17687")
//            .uri("bolt://localhost:7687")
            .database("neo4j")
//            .credentials("neo4j", "12345678")
            .useNativeTypes()
            .build();

    private static final SessionFactory SESSION_FACTORY = new SessionFactory(
            CONFIGURATION,
            "pt.bemanos.sports.coachvision.domain");

    private static final DatabaseFactory DATABASE_FACTORY = new DatabaseFactory();

    private DatabaseFactory() {
//        getSession().deleteAll(Event.class);
//        getSession().deleteAll(Game.class);
//        getSession().deleteAll(Player.class);
//        getSession().deleteAll(Team.class);
//        getSession().deleteAll(Attack.class);
    }

    public static DatabaseFactory getInstance() {
        return DATABASE_FACTORY;
    }

    public SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public Session getSession() {
        return SESSION_FACTORY.openSession();
    }
}
