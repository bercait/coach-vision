package pt.bemanos.sports.coachvision.services;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class DynamicTableServiceTest {

    DynamicTableService dynamicTableService = new DynamicTableService();

    @Test
    void getOrderOfColumnsFromQuery() {
        String query = "MATCH (g:Game)-[]-(p:Player)" +
                "CALL (p) {MATCH (p)<-[:ACTION]-(t:Throw) RETURN count(t) as Throws}" +
                "CALL (p) {MATCH (p)<-[:ACTION]-(t:Throw)-[:NEXT_EVENT]-(g:Goal) RETURN count(g) as Goals}" +
                "CALL (p) {MATCH (p)<-[:GOALKEEPER]-(s:Save) RETURN count(s) as Saves}" +
                "RETURN p.name as Name, Goals, Throws, Saves";

        List<String> columns = this.dynamicTableService.getOrderOfColumnsFromQuery(query);

        List<String> expected = List.of("Name", "Goals", "Throws", "Saves");
        assertIterableEquals(expected, columns);
    }
}