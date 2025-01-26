package pt.bemanos.sports.coachvision.domain;

import org.neo4j.ogm.annotation.Id;

public class Team {
    @Id
    private String name;

    public Team() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
