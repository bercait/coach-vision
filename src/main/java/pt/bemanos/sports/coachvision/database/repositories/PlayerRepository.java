package pt.bemanos.sports.coachvision.database.repositories;

import pt.bemanos.sports.coachvision.database.CrudRepository;
import pt.bemanos.sports.coachvision.domain.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerRepository extends CrudRepository<Player, Long> {
    @Override
    public Class<Player> getEntityType() {
        return Player.class;
    }

    public Player findByName(String teamName, String playerName) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("teamName", teamName);
        parameters.put("playerName", playerName);

        return session.queryForObject(
                getEntityType(),
                """
                        MATCH (t:Team {name:$teamName})
                        OPTIONAL MATCH (t)-[:HOME_TEAM]-(:Game)-[:HOME_PLAYER]-(hp:Player {name:$playerName})
                        OPTIONAL MATCH (t)-[:AWAY_TEAM]-(:Game)-[:AWAY_PLAYER]-(ap:Player {name:$playerName})
                        RETURN hp, ap
                        """,
                parameters
        );
    }

    public Player findOrCreate(String teamName, String playerName) {
        Player player = findByName(teamName, playerName);
        if (player != null) {
            return player;
        }

        player = new Player();
        player.setName(playerName);
        this.save(player);
        return player;
    }
}
