package pt.bemanos.sports.coachvision.controller;

import atlantafx.base.theme.Styles;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import pt.bemanos.sports.coachvision.services.DynamicTableService;

import java.net.URL;
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
        playersTable = dynamicTableService.createTableViewFromDynamicTable("player_total");

        AnchorPane.setTopAnchor(playersTable, 0d);
        AnchorPane.setBottomAnchor(playersTable, 0d);
        AnchorPane.setLeftAnchor(playersTable, 0d);
        AnchorPane.setRightAnchor(playersTable, 0d);

        Styles.toggleStyleClass(playersTable, Styles.BORDERED);
        Styles.toggleStyleClass(playersTable, Styles.STRIPED);

        playersTotalPane.getChildren().add(playersTable);
        observableList = playersTable.getItems();
    }
}
