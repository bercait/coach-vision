package pt.bemanos.sports.coachvision.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Event {
    @Relationship(type = "NEXT_EVENT")
    private Event nextEvent;
}
