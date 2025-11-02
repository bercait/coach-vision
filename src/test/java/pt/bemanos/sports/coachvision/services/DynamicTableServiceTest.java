package pt.bemanos.sports.coachvision.services;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class DynamicTableServiceTest {

    DynamicTableService dynamicTableService = new DynamicTableService();

    @Test
    void getOrderOfColumnsFromQuery() {
        String query = """
                CALL () {
                    MATCH (g:Game)-[:NEXT_ATTACK]-*(a:Attack)-[:NEXT_EVENT]-*(e:Event)-[]-(p:Player)-[:AWAY_PLAYER]-(g)-[:AWAY_TEAM]-(t:Team {name:$teamName})
                    RETURN *
                    UNION ALL
                    MATCH (g:Game)-[:NEXT_ATTACK]-*(a:Attack)-[:NEXT_EVENT]-*(e:Event)-[]-(p:Player)-[:HOME_PLAYER]-(g)-[:HOME_TEAM]-(t:Team {name:$teamName})
                    RETURN *
                }
                CALL (p) {MATCH (p)<-[:ACTOR]-(t:Throw) RETURN count(t) as Throws}
                CALL (p) {MATCH (p)<-[:ACTOR]-(t:Throw)-[:NEXT_EVENT]-(g:Goal) RETURN count(g) as Goals}
                CALL (p) {MATCH (p)<-[:ACTOR]-(t:Throw)-[:NEXT_EVENT]-(s:Save) RETURN count(s) as Saves}
                RETURN DISTINCT p.name AS Name,
                Goals,
                Throws,
                Saves,
                pt.bemanos.sports.coachvision.database.procedure.percentage(Goals,Throws) AS PG""";

        List<String> columns = this.dynamicTableService.getOrderOfColumnsFromQuery(query);

        List<String> expected = List.of("Name", "Goals", "Throws", "Saves", "PG");
        assertIterableEquals(expected, columns);
    }
}