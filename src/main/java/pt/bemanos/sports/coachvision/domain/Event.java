package pt.bemanos.sports.coachvision.domain;

import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
public class Event {
    @Id
    @GeneratedValue
    private Long id;
    @Labels
    private List<String> labels = new ArrayList<>();

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

    public List<String> getLabels() {
        return labels;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", labels=" + labels +
                ", nextEvent=" + nextEvent +
                '}';
    }
}
