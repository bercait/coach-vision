package pt.bemanos.sports.coachvision.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class Game {
    @Relationship(type = "HOME_PLAYER")
    private List<Player> homePlayers;

    @Relationship(type = "AWAY_PLAYER")
    private List<Player> awayPlayers;


    @Relationship(type = "NEXT_ATTACK")
    private Attack nextAttack;
}
