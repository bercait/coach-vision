package pt.bemanos.sports.coachvision;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class SplashScreen extends Preloader {

    private final StackPane parent = new StackPane();
    private Stage stage;

    @Override
    public void init() throws Exception {
        Image image = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("logo.png")));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(500);

        this.parent.getChildren().add(imageView);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        Scene scene = new Scene(this.parent, 640, 480);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == StateChangeNotification.Type.BEFORE_START) {
            this.stage.close();
            //Platform.exit();
        }
    }
}
