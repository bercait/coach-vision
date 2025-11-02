//package pt.bemanos.sports.coachvision.controller;
//
//import atlantafx.base.theme.Styles;
//import javafx.application.Platform;
//import javafx.beans.property.SimpleObjectProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.Node;
//import javafx.scene.control.TableCell;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.layout.AnchorPane;
//import pt.bemanos.sports.coachvision.services.DynamicTableService;
//
//import java.net.URL;
//import java.text.DecimalFormat;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.ResourceBundle;
//
//public class PlayerTotalController implements Initializable {
//    private final DynamicTableService dynamicTableService = new DynamicTableService();
//    @FXML
//    private AnchorPane playersTotalPane;
//    private TableView<Map<String, Object>> playersTable;
//    private ObservableList<Map<String, Object>> observableList;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println("Initializing PlayerTotalController");
//    }
//
//    @FXML
//    public void updateTeamSelected(String teamSelected) {
//        Platform.runLater(() -> {
//            playersTable = createTableViewFromDynamicTable(teamSelected);
//
//            AnchorPane.setTopAnchor(playersTable, 0d);
//            AnchorPane.setBottomAnchor(playersTable, 0d);
//            AnchorPane.setLeftAnchor(playersTable, 0d);
//            AnchorPane.setRightAnchor(playersTable, 0d);
//
//            Styles.toggleStyleClass(playersTable, Styles.BORDERED);
//            Styles.toggleStyleClass(playersTable, Styles.STRIPED);
//
//            playersTotalPane.getChildren().add(playersTable);
//        });
//    }
//
//    private TableView<Map<String, Object>> createTableViewFromDynamicTable(String teamName) {
//        Map<String, String> parameters = new HashMap<>();
/// /        parameters.put("teamName", "CJAG SENIORES");
//        parameters.put("teamName", teamName);
//        TableView<Map<String, Object>> tableView = new TableView<>();
//        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
//
//        Map<String, String> columnNames = new LinkedHashMap<>();
//        columnNames.put("Name", "Nome");
//        columnNames.put("Actions", "AO");
//        columnNames.put("Goals", "G");
//        columnNames.put("Throws", "R");
//        columnNames.put("Penalty", "7m");
////        columnNames.put("Assists", "A");
//        columnNames.put("Turnover", "FT");
//        columnNames.put("PA", "% Ata");
//        columnNames.put("PT", "% Rem");
//        columnNames.put("P7", "% 7m");
//
//
//        columnNames.forEach((key, header) -> {
//            TableColumn<Map<String, Object>, Object> column = new TableColumn<>(header);
//            column.setCellValueFactory(map -> new SimpleObjectProperty<>(map.getValue().get(key)));
//            column.setCellFactory(mapObjectTableColumn -> {
//                if (key.equalsIgnoreCase("PA") || key.equalsIgnoreCase("PT")) {
//                    return new PercentageCell();
//                }
//
//                return new DynamicCell();
//            });
//            tableView.getColumns().add(column);
//        });
//
//        observableList = FXCollections.observableList(dynamicTableService.getDataFromDynamicTable("player_total", parameters));
//        tableView.setItems(observableList);
//        return tableView;
//    }
//
//    static class PercentageCell extends DynamicCell {
//        @Override
//        public String buildText(Object item, boolean empty) {
//            if (item == null || empty) {
//                return null;
//            }
//
//            double value = (double) item;
//            if (value == -1) {
//                return null;
//            }
//
//            DecimalFormat df = new DecimalFormat("0'%'");
//            return df.format(value);
//        }
//    }
//
//    static class DynamicCell extends TableCell<Map<String, Object>, Object> {
//
//        public String buildText(Object item, boolean empty) {
//            if (item == null || empty) {
//                return null;
//            }
//
//            return item.toString();
//        }
//
//        public Node buildGraphic(Object item) {
//            return null;
//        }
//
//        public String buildStyle(Object item) {
//            return "";
//        }
//
//        @Override
//        protected void updateItem(Object item, boolean empty) {
//            super.updateItem(item, empty);
//
//            setText(buildText(item, empty));
//            setGraphic(buildGraphic(item));
//            setStyle(buildStyle(item));
//        }
//    }
//}
