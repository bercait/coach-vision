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

        dynamicTable.getProperties().forEach((key, value) -> {
            String query = cypher.get()
                    .concat(dynamicTable.getCypherCall())
                    .concat("{")
                    .concat(value)
                    .concat(" as ")
                    .concat(key)
                    .concat("}");
            cypher.set(query);

        });
        cypher.set(cypher.get().concat(dynamicTable.getCypherReturn()));

        return cypher.get();
    }

    protected List<String> getOrderOfColumnsFromQuery(String query) {
        String[] returnSplit = query.split("RETURN");
        String[] rawColumns = returnSplit[returnSplit.length - 1].split(",\n");
        final String splitter = " AS ";

        return Arrays.stream(rawColumns)
                .map(value -> {
                    if (value.contains(splitter)) {
                        String[] split = value.split(splitter);
                        value = split[split.length - 1];
                    }

                    return value.trim();
                }).toList();
    }

    public List<Map<String, Object>> getDataFromDynamicTable(String tableId, Map<String, ?> parameters) {
        String cypherQuery = this.getQueryFromDynamicTable(tableId);
        List<String> columns = this.getOrderOfColumnsFromQuery(cypherQuery);
        List<Map<String, Object>> data = new ArrayList<>();

        Session session = DatabaseFactory.getInstance().getSession();
        Result result = session.query(cypherQuery, parameters);

        result.forEach(map -> {
            Map<String, Object> values = new HashMap<>();
            columns.forEach(column -> {
                Object object = map.get(column);
                String value = String.valueOf(object);
                values.put(column, object);
            });
            data.add(values);
        });

        return data;
    }
}
