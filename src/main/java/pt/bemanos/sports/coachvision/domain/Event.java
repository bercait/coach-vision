package pt.bemanos.sports.coachvision.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "NEXT_EVENT")
    private Event nextEvent;

    public Event() {
    }

    public Event getNextEvent() {
        return nextEvent;
    }

    public void setNextEvent(Event nextEvent) {
        this.nextEvent = nextEvent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", nextEvent=" + nextEvent +
                '}';
    }
}
