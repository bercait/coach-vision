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
import org.neo4j.ogm.session.Session;
import pt.bemanos.sports.coachvision.database.DatabaseFactory;
import pt.bemanos.sports.coachvision.database.ServerFactory;
import pt.bemanos.sports.coachvision.domain.Player;

/**
 * JavaFX App
 */
public class ApplicationFX extends Application {



    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("Neo4j server started with database: " + ServerFactory.getInstance().getGraphDatabase().databaseName());
        // Registers a shutdown hook for the Neo4j instance so that it shuts down nicely when the VM exits
        // (even if you "Ctrl-C" the running application).
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseFactory.getInstance().getSessionFactory().close();
            ServerFactory.getInstance().getManagementService().shutdown();
        }));

        Player player = new Player();
        player.setName("Player Test");
        Session session = DatabaseFactory.getInstance().getSession();
        session.save(player);
    }

    @Override
    public void start(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        var antialiasing = Platform.isSupported(ConditionalFeature.SCENE3D)
                ? SceneAntialiasing.BALANCED
                : SceneAntialiasing.DISABLED;

        Session session = DatabaseFactory.getInstance().getSession();
        Player player = session.loadAll(Player.class).stream().findFirst().get();

        var label = new Label("Hello, " + player.getName() + ", with id " + player.getId() + ".");
        var scene = new Scene(new StackPane(label), 640, 480, false, antialiasing);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        DatabaseFactory.getInstance().getSessionFactory().close();
        ServerFactory.getInstance().getManagementService().shutdown();
        Platform.exit();
    }

}