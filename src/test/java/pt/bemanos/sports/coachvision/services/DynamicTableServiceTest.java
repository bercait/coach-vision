package pt.bemanos.sports.coachvision.services;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class DynamicTableServiceTest {

    DynamicTableService dynamicTableService = new DynamicTableService();

    @Test
    void getOrderOfColumnsFromQuery() {
        String query = "MATCH (g:Game)-[]-(p:Player)" +
                "CALL (p) {MATCH (p)<-[:ACTION]-(t:Throw) RETURN count(t) as throws}" +
                "CALL (p) {MATCH (p)<-[:ACTION]-(t:Throw)-[:NEXT_EVENT]-(g:Goal) RETURN count(g) as goals}" +
                "CALL (p) {MATCH (p)<-[:GOALKEEPER]-(s:Save) RETURN count(s) as saves}" +
                "RETURN p.name as name, goals, throws, saves";

        List<String> columns = this.dynamicTableService.getOrderOfColumnsFromQuery(query);

        List<String> expected = List.of("name", "goals", "throws", "saves");
        assertIterableEquals(expected, columns);
    }
}