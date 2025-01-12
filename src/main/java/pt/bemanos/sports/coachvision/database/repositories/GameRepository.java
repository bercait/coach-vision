package pt.bemanos.sports.coachvision.database.repositories;

import pt.bemanos.sports.coachvision.database.CrudRepository;
import pt.bemanos.sports.coachvision.domain.Game;

public class GameRepository extends CrudRepository<Game, Long> {
    @Override
    public Class<Game> getEntityType() {
        return Game.class;
    }
}
