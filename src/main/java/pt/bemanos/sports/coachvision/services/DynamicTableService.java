package pt.bemanos.sports.coachvision.services;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import pt.bemanos.sports.coachvision.database.DatabaseFactory;
import pt.bemanos.sports.coachvision.database.repositories.DynamicTableRepository;
import pt.bemanos.sports.coachvision.domain.DynamicTable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class DynamicTableService {

    private final DynamicTableRepository dynamicTableRepository = new DynamicTableRepository();

    private String getQueryFromDynamicTable(String tableId) {
        DynamicTable dynamicTable = dynamicTableRepository.findById(tableId);

        final AtomicReference<String> cypher = new AtomicReference<>(dynamicTable.getCypherMatch());
        final AtomicReference<String> returnQuery = new AtomicReference<>(dynamicTable.getCypherReturn());

        dynamicTable.getProperties().forEach((key, value) -> {
            String query = cypher.get()
                    .concat(dynamicTable.getCypherCall())
                    .concat("{")
                    .concat(value)
                    .concat(" as ")
                    .concat(key)
                    .concat("}");
            cypher.set(query);

            returnQuery.set(returnQuery.get().concat(", ").concat(key));
        });
        cypher.set(cypher.get().concat(returnQuery.get()));

        return cypher.get();
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

    public List<Map<String, String>> getDataFromDynamicTable(String tableId, Map<String, ?> parameters) {
        String cypherQuery = this.getQueryFromDynamicTable(tableId);
        List<String> columns = this.getOrderOfColumnsFromQuery(cypherQuery);
        List<Map<String, String>> data = new ArrayList<>();

        Session session = DatabaseFactory.getInstance().getSession();
        Result result = session.query(cypherQuery, parameters);

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
}
