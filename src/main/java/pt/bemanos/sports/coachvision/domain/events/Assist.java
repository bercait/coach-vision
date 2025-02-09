package pt.bemanos.sports.coachvision.domain.events;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;
import pt.bemanos.sports.coachvision.domain.Event;
import pt.bemanos.sports.coachvision.domain.Player;

public class Assist extends Event {
    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "ACTOR")
    private Player actor;

    public Assist() {
    }

    public Player getActor() {
        return actor;
    }

    public void setActor(Player actor) {
        this.actor = actor;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Assist{" +
                "id=" + id +
                ", actor=" + actor +
                '}';
    }
}
