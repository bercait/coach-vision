package pt.bemanos.sports.coachvision.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax0.license3j.HardwareBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.bemanos.sports.coachvision.ApplicationFX;

import java.awt.*;
import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

public class RequestLicenseController {

    @FXML
    private TextField machineId;

    Logger logger = LoggerFactory.getLogger(ApplicationFX.class.getName());

    @FXML
    public void initialize() {
        logger.info("Initializing RequestLicenseController");
        try {
            this.machineId.setText(new HardwareBinder().getMachineIdString());
        } catch (NoSuchAlgorithmException | SocketException | UnknownHostException e) {
            logger.error("Error:", e);
            throw new RuntimeException(e);
        }
    }

    public void openBrowser(ActionEvent actionEvent) {
        logger.info("Opening browser");
        try {
            Desktop.getDesktop().browse(new URI("https://forms.gle/RZKnvSKoCF9imjZM7"));
        } catch (IOException | URISyntaxException e) {
            logger.error("Error:", e);
            throw new RuntimeException(e);
        }
    }

    public void closeWindow(ActionEvent actionEvent) {
        logger.info("Closing Window");
        Stage stage = (Stage) machineId.getScene().getWindow();
        stage.close();
    }
}
