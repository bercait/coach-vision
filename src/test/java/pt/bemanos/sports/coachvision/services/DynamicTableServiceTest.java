package pt.bemanos.sports.coachvision.services;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class DynamicTableServiceTest {

    DynamicTableService dynamicTableService = new DynamicTableService();

    @Test
    void getOrderOfColumnsFromQuery() {
        String query = "MATCH (g:Game)-[:NEXT_ATTACK]-+(a:Attack)-[:NEXT_EVENT]-+(e:Event)-[]-(p:Player)-[]-(g)-[]-(t:Team)" +
                "CALL (p) {MATCH (p)<-[:ACTOR]-(t:Throw) RETURN count(t) as Throws}" +
                "CALL (p) {MATCH (p)<-[:ACTOR]-(t:Throw)-[:NEXT_EVENT]-(g:Goal) RETURN count(g) as Goals}" +
                "CALL (p) {MATCH (p)<-[:ACTOR]-(t:Throw)-[:NEXT_EVENT]-(s:Save) RETURN count(s) as Saves}" +
                "RETURN DISTINCT p.name as Name, Goals, Throws, Saves";

        List<String> columns = this.dynamicTableService.getOrderOfColumnsFromQuery(query);

        List<String> expected = List.of("Name", "Goals", "Throws", "Saves");
        assertIterableEquals(expected, columns);
    }
}