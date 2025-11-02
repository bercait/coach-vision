package pt.bemanos.sports.coachvision.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax0.license3j.HardwareBinder;

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

    @FXML
    public void initialize() {
        try {
            this.machineId.setText(new HardwareBinder().getMachineIdString());
        } catch (NoSuchAlgorithmException | SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public void openBrowser(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://forms.gle/RZKnvSKoCF9imjZM7"));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) machineId.getScene().getWindow();
        stage.close();
    }
}
