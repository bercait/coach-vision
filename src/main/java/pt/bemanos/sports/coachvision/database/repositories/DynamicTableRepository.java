package pt.bemanos.sports.coachvision.database.repositories;

import pt.bemanos.sports.coachvision.database.CrudRepository;
import pt.bemanos.sports.coachvision.domain.DynamicTable;

public class DynamicTableRepository extends CrudRepository<DynamicTable, String> {
    @Override
    public Class<DynamicTable> getEntityType() {
        return DynamicTable.class;
    }
}
