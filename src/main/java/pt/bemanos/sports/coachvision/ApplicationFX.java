package pt.bemanos.sports.coachvision;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pt.bemanos.sports.coachvision.database.DatabaseFactory;
import pt.bemanos.sports.coachvision.database.ServerFactory;

import java.io.IOException;

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
    }

    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        var antialiasing = Platform.isSupported(ConditionalFeature.SCENE3D)
                ? SceneAntialiasing.BALANCED
                : SceneAntialiasing.DISABLED;

        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationFX.class.getResource("/fxml/player_total.fxml"));
        Pane pane = fxmlLoader.load();

        var scene = new Scene(pane, 800, 600, false, antialiasing);
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