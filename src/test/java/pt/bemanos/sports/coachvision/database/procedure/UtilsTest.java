package pt.bemanos.sports.coachvision.database.procedure;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UtilsTest {
    private Neo4j embeddedDatabaseServer;

    @BeforeAll
    void initializeNeo4j() {
        this.embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder().withDisabledServer().withFunction(Utils.class).build();
    }

    @AfterAll
    void closeNeo4j() {
        this.embeddedDatabaseServer.close();
    }

    @Test
    void percentage() {
        // This is in a try-block, to make sure we close the driver after the test
        try (Driver driver = GraphDatabase.driver(embeddedDatabaseServer.boltURI()); Session session = driver.session()) {

            // When
            Double result = session
                    .run("RETURN pt.bemanos.sports.coachvision.database.procedure.percentage(5, 10) AS result")
                    .single()
                    .get("result")
                    .asDouble();

            // Then
            assertThat(result).isEqualTo(50.0);
        }
    }

    @Test
    void percentageDividerZero() {
        // This is in a try-block, to make sure we close the driver after the test
        try (Driver driver = GraphDatabase.driver(embeddedDatabaseServer.boltURI()); Session session = driver.session()) {

            // When
            Double result = session
                    .run("RETURN pt.bemanos.sports.coachvision.database.procedure.percentage(5, 0) AS result")
                    .single()
                    .get("result")
                    .asDouble();

            // Then
            assertThat(result).isEqualTo(-1);
        }
    }
}
