package pt.bemanos.sports.coachvision.controller;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.bemanos.sports.coachvision.ApplicationFX;
import pt.bemanos.sports.coachvision.database.DatabaseFactory;
import pt.bemanos.sports.coachvision.services.DynamicTableService;
import pt.bemanos.sports.coachvision.services.imports.ImportXpsService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ReportMatchController implements Initializable {
    private final DynamicTableService dynamicTableService = new DynamicTableService();
    @FXML
    private AnchorPane reportPane;
    @FXML
    private TableView<Map<String, Object>> goalkeeperTotals;
    @FXML
    private Label fileNameLabel;
    @FXML
    private ChoiceBox<String> teamsSelect;
    private ObservableList<Map<String, Object>> playersList = FXCollections.emptyObservableList();

    Logger logger = LoggerFactory.getLogger(ApplicationFX.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initializing ReportMatchController");

        teamsSelect.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    logger.info("Selected Team: {}", newValue);
                    this.updateTeamSelected(newValue);
                });
    }

    public void loadFile(ActionEvent actionEvent) {
        logger.info("Loading File");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Csv Files", "*.csv"));

        Window window = ((Node) actionEvent.getTarget()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(window);

        if (file != null) {
            logger.info("File Loaded " + file.getAbsolutePath());
            Platform.runLater(() -> {
                this.updateUI(file.getName());
                try {
                    logger.info("Clearing DB");
                    DatabaseFactory.getInstance().getSession().clear();

                    logger.info("Importing Events from File");
                    ImportXpsService importXpsService = new ImportXpsService(file.getAbsolutePath());
                    importXpsService.importEvents();

                    logger.info("Getting Teams");
                    teamsSelect.getItems().addAll(importXpsService.getAllTeams());
                } catch (IOException e) {
                    //TODO Exception handling
                    logger.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            });
        } else {
            logger.info("No file selected");
            this.updateUI("");
        }
    }

    private void updateUI(String fileName) {
        if (fileName.isBlank()) {
            fileNameLabel.setText("");
            teamsSelect.setDisable(true);
        } else {
            fileNameLabel.setText(fileName);
            teamsSelect.setDisable(false);
        }
    }

    public void updateTeamSelected(String teamSelected) {
        Platform.runLater(() -> {
            TableView<Map<String, Object>> playersTotal = createTableViewFromDynamicTable(teamSelected);

            AnchorPane.setTopAnchor(playersTotal, 65d);
            AnchorPane.setBottomAnchor(playersTotal, 150d);
            AnchorPane.setLeftAnchor(playersTotal, 0d);
            AnchorPane.setRightAnchor(playersTotal, 250d);

            Styles.toggleStyleClass(playersTotal, Styles.BORDERED);
            Styles.toggleStyleClass(playersTotal, Styles.STRIPED);

            reportPane.getChildren().add(playersTotal);
        });
    }

    private TableView<Map<String, Object>> createTableViewFromDynamicTable(String teamName) {
        Map<String, String> parameters = new HashMap<>();
//        parameters.put("teamName", "CJAG SENIORES");
        parameters.put("teamName", teamName);
        TableView<Map<String, Object>> playersTotal = new TableView<>();
        playersTotal.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        Map<String, String> columnNames = new LinkedHashMap<>();
        columnNames.put("Name", "Nome");
        columnNames.put("Actions", "AO");
        columnNames.put("Goals", "G");
        columnNames.put("Throws", "R");
        columnNames.put("Penalty", "7m");
//        columnNames.put("Assists", "A");
        columnNames.put("Turnover", "FT");
        columnNames.put("PA", "% Ata");
        columnNames.put("PT", "% Rem");
        columnNames.put("P7", "% 7m");


        columnNames.forEach((key, header) -> {
            TableColumn<Map<String, Object>, Object> column = new TableColumn<>(header);
            column.setCellValueFactory(map -> new SimpleObjectProperty<>(map.getValue().get(key)));
            column.setCellFactory(mapObjectTableColumn -> {
                if (key.equalsIgnoreCase("PA") || key.equalsIgnoreCase("PT")) {
                    return new PercentageCell();
                }

                return new DynamicCell();
            });
            playersTotal.getColumns().add(column);
        });

        playersList = FXCollections.observableList(dynamicTableService.getDataFromDynamicTable("player_total", parameters));
        playersTotal.setItems(playersList);

        return playersTotal;
    }

    static class PercentageCell extends DynamicCell {
        @Override
        public String buildText(Object item, boolean empty) {
            if (item == null || empty) {
                return null;
            }

            double value = (double) item;
            if (value == -1) {
                return null;
            }

            DecimalFormat df = new DecimalFormat("0'%'");
            return df.format(value);
        }
    }

    static class DynamicCell extends TableCell<Map<String, Object>, Object> {

        public String buildText(Object item, boolean empty) {
            if (item == null || empty) {
                return null;
            }

            return item.toString();
        }

        public Node buildGraphic(Object item) {
            return null;
        }

        public String buildStyle(Object item) {
            return "";
        }

        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);

            setText(buildText(item, empty));
            setGraphic(buildGraphic(item));
            setStyle(buildStyle(item));
        }
    }
}
