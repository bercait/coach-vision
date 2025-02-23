package pt.bemanos.sports.coachvision.database.procedure;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class Utils {
    @UserFunction
    @Description("calculate percentage.")
    public Double percentage(
            @Name("value") Double value,
            @Name("total") Double total
    ) {
        if (total == 0) {
            return -1d;
        }

        return value / total * 100;
    }
}
