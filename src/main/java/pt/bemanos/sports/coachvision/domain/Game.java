package pt.bemanos.sports.coachvision.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public class Game {
    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "HOME_PLAYER")
    private List<Player> homePlayers;

    @Relationship(type = "AWAY_PLAYER")
    private List<Player> awayPlayers;

    @Relationship(type = "HOME_TEAM")
    private Team homeTeam;

    @Relationship(type = "AWAY_TEAM")
    private Team awayTeam;

    @Relationship(type = "NEXT_ATTACK")
    private Attack nextAttack;

    public Game() {
    }

    public List<Player> getHomePlayers() {
        return homePlayers;
    }

    public void setHomePlayers(List<Player> homePlayers) {
        this.homePlayers = homePlayers;
    }

    public List<Player> getAwayPlayers() {
        return awayPlayers;
    }

    public void setAwayPlayers(List<Player> awayPlayers) {
        this.awayPlayers = awayPlayers;
    }

    public Attack getNextAttack() {
        return nextAttack;
    }

    public void setNextAttack(Attack nextAttack) {
        this.nextAttack = nextAttack;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", homePlayers=" + homePlayers +
                ", awayPlayers=" + awayPlayers +
                ", homeTeam=" + homeTeam +
                ", awayTeam=" + awayTeam +
                ", nextAttack=" + nextAttack +
                '}';
    }
}
