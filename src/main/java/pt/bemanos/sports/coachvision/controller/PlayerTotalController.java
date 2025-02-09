package pt.bemanos.sports.coachvision.controller;

import atlantafx.base.theme.Styles;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import pt.bemanos.sports.coachvision.services.DynamicTableService;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PlayerTotalController implements Initializable {
    private final DynamicTableService dynamicTableService = new DynamicTableService();
    @FXML
    private AnchorPane playersTotalPane;
    private TableView<Map<String, String>> playersTable;
    private ObservableList<Map<String, String>> observableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playersTable = createTableViewFromDynamicTable();

        AnchorPane.setTopAnchor(playersTable, 0d);
        AnchorPane.setBottomAnchor(playersTable, 0d);
        AnchorPane.setLeftAnchor(playersTable, 0d);
        AnchorPane.setRightAnchor(playersTable, 0d);

        Styles.toggleStyleClass(playersTable, Styles.BORDERED);
        Styles.toggleStyleClass(playersTable, Styles.STRIPED);

        playersTotalPane.getChildren().add(playersTable);
        observableList = playersTable.getItems();
    }

    private TableView<Map<String, String>> createTableViewFromDynamicTable() {
        List<Map<String, String>> data = dynamicTableService.getDataFromDynamicTable("player_total");
        TableView<Map<String, String>> tableView = new TableView<>();

        data.get(0).forEach((key, value) -> {
            TableColumn<Map<String, String>, String> column = new TableColumn<>(key);
            column.setCellValueFactory(map -> new SimpleStringProperty(map.getValue().get(key)));
            tableView.getColumns().add(0, column);
        });

        TableColumn<Map<String, String>, String> column = new TableColumn<>("%\nRem");
        column.setCellValueFactory(map ->
                new SimpleStringProperty(calculatePercentage(map.getValue().get("Goals"), map.getValue().get("Throws"))));
        tableView.getColumns().add(column);


        ObservableList<Map<String, String>> observableList = FXCollections.observableList(data);
        tableView.setItems(observableList);
        return tableView;
    }

    private String calculatePercentage(String valueStr, String totalStr) {
        String result = "";
        float value = Float.parseFloat(valueStr);
        float total = Float.parseFloat(totalStr);

        DecimalFormat df = new DecimalFormat("0");
        df.setMaximumFractionDigits(0);
        if (total > 0) {
            result = String.format(df.format(value / total * 100));
        }
        return result;
    }
}
