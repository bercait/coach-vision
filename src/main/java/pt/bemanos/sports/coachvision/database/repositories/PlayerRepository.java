package pt.bemanos.sports.coachvision.database.repositories;

import pt.bemanos.sports.coachvision.database.CrudRepository;
import pt.bemanos.sports.coachvision.domain.Player;

public class PlayerRepository extends CrudRepository<Player, Long> {
    @Override
    public Class<Player> getEntityType() {
        return Player.class;
    }
}
