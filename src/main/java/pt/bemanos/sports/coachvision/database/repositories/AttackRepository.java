package pt.bemanos.sports.coachvision.database.repositories;

import pt.bemanos.sports.coachvision.database.CrudRepository;
import pt.bemanos.sports.coachvision.domain.Attack;

public class AttackRepository extends CrudRepository<Attack, Long> {
    @Override
    public Class<Attack> getEntityType() {
        return Attack.class;
    }
}
