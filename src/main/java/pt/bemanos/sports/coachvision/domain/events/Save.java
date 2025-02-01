package pt.bemanos.sports.coachvision.domain.events;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;
import pt.bemanos.sports.coachvision.domain.Event;
import pt.bemanos.sports.coachvision.domain.Player;

public class Save extends Event {
    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "GOALKEEPER")
    private Player goalkeeper;

    public Save() {
    }

    public Player getGoalkeeper() {
        return goalkeeper;
    }

    public void setGoalkeeper(Player goalkeeper) {
        this.goalkeeper = goalkeeper;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Save{" +
                "id=" + id +
                ", goalkeeper=" + goalkeeper +
                '}';
    }
}
