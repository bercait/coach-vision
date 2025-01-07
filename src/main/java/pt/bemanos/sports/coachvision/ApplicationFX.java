package pt.bemanos.sports.coachvision;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import pt.bemanos.sports.coachvision.database.DatabaseFactory;
import pt.bemanos.sports.coachvision.domain.Player;

/**
 * JavaFX App
 */
public class ApplicationFX extends Application {

    Configuration config = new Configuration.Builder()
            .uri("bolt://localhost:7687")
            .build();
    SessionFactory sessionFactory = new SessionFactory(config, "pt.bemanos.sports.coachvision.domain");

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("Connected to database: " + DatabaseFactory.getInstance().getGraphDatabase().databaseName());
        // Registers a shutdown hook for the Neo4j instance so that it shuts down nicely when the VM exits
        // (even if you "Ctrl-C" the running application).
        Runtime.getRuntime().addShutdownHook(new Thread(() -> DatabaseFactory.getInstance().getManagementService().shutdown()));

        Player player = new Player();
        player.setName("Player Test");
        Session session = sessionFactory.openSession();
        session.save(player);
    }

    @Override
    public void start(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        var antialiasing = Platform.isSupported(ConditionalFeature.SCENE3D)
                ? SceneAntialiasing.BALANCED
                : SceneAntialiasing.DISABLED;

        Session session = sessionFactory.openSession();
        Player player = session.loadAll(Player.class).stream().findFirst().get();

        var label = new Label("Hello, " + player.getName() + ", with id " + player.getId() + ".");
        var scene = new Scene(new StackPane(label), 640, 480, false, antialiasing);
        stage.setScene(scene);
        stage.show();

        sessionFactory.close();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();

        sessionFactory.close();
        DatabaseFactory.getInstance().getManagementService().shutdown();
    }

}