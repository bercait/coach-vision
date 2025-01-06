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
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import pt.bemanos.sports.coachvision.database.DatabaseFactory;

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

        try (Transaction tx = DatabaseFactory.getInstance().getGraphDatabase().beginTx()) {
            Node node = tx.createNode(org.neo4j.graphdb.Label.label("Player"));
            node.setProperty("name", "Player Test");
            node.setProperty("id", 0);
            tx.commit();
        }
    }

    @Override
    public void start(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        var antialiasing = Platform.isSupported(ConditionalFeature.SCENE3D)
                ? SceneAntialiasing.BALANCED
                : SceneAntialiasing.DISABLED;

        int id;
        String name;
        try (Transaction tx = DatabaseFactory.getInstance().getGraphDatabase().beginTx()) {
            Node node = tx.getAllNodes().stream().findFirst().get();

            id = (int) node.getProperty("id");
            name = (String) node.getProperty("name");
        }

        var label = new Label("Hello, " + name + ", with id " + id + ".");
        var scene = new Scene(new StackPane(label), 640, 480, false, antialiasing);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();
    }

}