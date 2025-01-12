package pt.bemanos.sports.coachvision.services;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import pt.bemanos.sports.coachvision.database.DatabaseFactory;

import java.util.*;

public class DynamicTableService {

    private String getQueryFromDynamicTable(String tableId) {
        return "MATCH (g:Game)-[]-(p:Player)" +
                "CALL (p) {MATCH (p)<-[:ACTION]-(t:Throw) RETURN count(t) as Throws}" +
                "CALL (p) {MATCH (p)<-[:ACTION]-(t:Throw)-[:NEXT_EVENT]-(g:Goal) RETURN count(g) as Goals}" +
                "CALL (p) {MATCH (p)<-[:GOALKEEPER]-(s:Save) RETURN count(s) as Saves}" +
                "RETURN p.name as Name, Goals, Throws, Saves";
    }

    protected List<String> getOrderOfColumnsFromQuery(String query) {
        String[] returnSplit = query.split("RETURN");
        String[] rawColumns = returnSplit[returnSplit.length - 1].split(",");

        return Arrays.stream(rawColumns)
                .map(value -> {
                    if (value.contains(" as ")) {
                        String[] split = value.split(" ");
                        value = split[split.length - 1];
                    }

                    return value.trim();
                }).toList();
    }

    public List<Map<String, String>> getDataFromDynamicTable(String tableId) {
        String cypherQuery = this.getQueryFromDynamicTable(tableId);
        List<String> columns = this.getOrderOfColumnsFromQuery(cypherQuery);
        List<Map<String, String>> data = new ArrayList<>();

        Session session = DatabaseFactory.getInstance().getSession();
        Result result = session.query(cypherQuery, new HashMap<>());

        result.forEach(map -> {
            Map<String, String> values = new HashMap<>();
            columns.forEach(column -> {
                Object object = map.get(column);
                String value = String.valueOf(object);
                values.put(column, value);
            });
            data.add(values);
        });

        return data;
    }


    public TableView<Map<String, String>> createTableViewFromDynamicTable(String tableId) {
        List<Map<String, String>> data = this.getDataFromDynamicTable(tableId);
        TableView<Map<String, String>> tableView = new TableView<>();

        data.get(0).forEach((key, value) -> {
            TableColumn<Map<String, String>, String> column = new TableColumn<>(key);
            column.setCellValueFactory(map -> new SimpleStringProperty(map.getValue().get(key)));
            tableView.getColumns().add(0, column);
        });

        ObservableList<Map<String, String>> observableList = FXCollections.observableList(data);
        tableView.setItems(observableList);
        return tableView;
    }
}
