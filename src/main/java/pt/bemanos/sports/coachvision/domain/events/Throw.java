package pt.bemanos.sports.coachvision.domain.events;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;
import pt.bemanos.sports.coachvision.domain.Event;
import pt.bemanos.sports.coachvision.domain.Player;

public class Throw extends Event {
    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "ACTOR")
    private Player actor;

    public Throw() {
    }

    public Player getActor() {
        return actor;
    }

    public void setActor(Player actor) {
        this.actor = actor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
